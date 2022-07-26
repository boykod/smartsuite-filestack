package ua.motionman.filestack.ui.filestacksources

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.filestack.Sources
import ua.motionman.filestack.R
import ua.motionman.filestack.databinding.SourcesFragmentBinding
import ua.motionman.filestack.domain.model.SourceModel
import ua.motionman.filestack.domain.model.UploadResult
import ua.motionman.filestack.ui.uploadingprogress.UploadingProgressFragment.Companion.SELECTIONS_KEY
import ua.motionman.filestack.utils.delegate.viewBinding
import ua.motionman.filestack.utils.extensions.toSelection
import ua.motionman.filestack.utils.source.localSourceData

class SourcesFragment : Fragment(R.layout.sources_fragment) {

    private val binding by viewBinding(SourcesFragmentBinding::bind)

    private val onSourceSelected: SourceSelected = { handleSourceSelected(it) }

    private val sourceAdapter = SourcesAdapter(onSourceSelected)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleArguments()
        setToolbarTitle()
        initAdapter()

        sourceAdapter.submitList(localSourceData)
    }

    private fun handleArguments() {
        val isComplete = arguments?.getBoolean(COMPLETE_UPLOAD_KEY) ?: false

        if (isComplete) {
            val result = arguments?.getSerializable(UPLOAD_RESULT_KEY) ?: emptyArray<UploadResult>()
            val intent = Intent().apply {
                putExtra(BUNDLE_RESULT_KEY, result)
            }
            activity?.setResult(Activity.RESULT_OK, intent)
            activity?.finish()
        }
    }

    private fun setToolbarTitle() {
        binding.sourcesToolbar.toolbarTitleTextView.text = getText(R.string.source_title)
    }

    private fun initAdapter() {
        binding.sourcesRecyclerView.apply {
            adapter = sourceAdapter
        }
    }

    private fun handleSourceSelected(source: SourceModel.Source) {
        when (source.type) {
            Sources.DEVICE -> proceedWithDeviceContentPicker()
            Sources.CAMERA -> proceedWithCamera()
        }
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                findNavController().navigate(R.id.sourcesToUploading, Bundle().apply {
                    putSerializable(SELECTIONS_KEY, result.toSelection(requireContext()))
                })
            }
        }

    private fun proceedWithDeviceContentPicker() {
        val mimeTypes: Array<String> = arrayOf("application/pdf", "video/*", "image/*")

        Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            type = "*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }.also {
            startForResult.launch(it)
        }
    }

    private fun proceedWithCamera() {
        findNavController().navigate(R.id.sourceToCamera)
    }

    companion object {
        const val COMPLETE_UPLOAD_KEY = "complete_upload"
        const val UPLOAD_RESULT_KEY = "upload_result"
        const val BUNDLE_RESULT_KEY = "bundle_result"
    }
}