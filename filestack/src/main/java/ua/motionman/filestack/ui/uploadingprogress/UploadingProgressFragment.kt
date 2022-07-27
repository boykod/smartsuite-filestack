package ua.motionman.filestack.ui.uploadingprogress

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import ua.motionman.filestack.R
import ua.motionman.filestack.databinding.UploadingProgressBinding
import ua.motionman.filestack.domain.model.Selection
import ua.motionman.filestack.domain.model.UploadResult
import ua.motionman.filestack.ui.filestacksources.SourcesFragment
import ua.motionman.filestack.utils.delegate.viewBinding

class UploadingProgressFragment : Fragment(R.layout.uploading_progress) {

    private val binding by viewBinding(UploadingProgressBinding::bind)

    private val vm: UploadingProgressViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        initFlowSubscriber()
        startUploadService()
    }

    private fun setupListeners() {
        binding.cancelButton.setOnClickListener { handleOnCancel() }
    }

    private fun initFlowSubscriber() {
        lifecycleScope.launchWhenStarted {
            vm.viewState.collect { updateUi(it) }
        }
        lifecycleScope.launchWhenStarted {
            vm.uploadEvent.collect { handleOnFinish(it.uploadResult) }
        }
    }

    private fun updateUi(state: UiState) {
        with(binding) {
            progressBar.setProgress(state.progress, true)
            uploadingTitle.text =
                String.format("Uploaded files: %d/%d", state.currentFile, state.totalFiles)
        }
    }

    private fun startUploadService() {
        val selections = arguments?.getSerializable(SELECTIONS_KEY) as? Array<Selection>
        vm.uploadSelections(selections, requireContext().contentResolver)
    }

    private fun handleOnCancel() {
        vm.cancelUpload()
        findNavController().navigate(R.id.uploadingToSources)
    }

    private fun handleOnFinish(result: Array<UploadResult> = emptyArray()) {
        vm.cancelUpload()
        val intent = Intent().apply {
            putExtra(SourcesFragment.BUNDLE_RESULT_KEY, result)
        }
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    companion object {
        const val SELECTIONS_KEY = "selections_bundle_key"
    }

}