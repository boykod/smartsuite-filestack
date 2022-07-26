package ua.motionman.filestack.ui.uploadingprogress

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import ua.motionman.filestack.R
import ua.motionman.filestack.databinding.UploadingProgressBinding
import ua.motionman.filestack.domain.model.Selection
import ua.motionman.filestack.ui.filestacksources.SourcesFragment.Companion.COMPLETE_UPLOAD_KEY
import ua.motionman.filestack.utils.delegate.viewBinding

class UploadingProgressFragment : Fragment(R.layout.uploading_progress) {

    private val binding by viewBinding(UploadingProgressBinding::bind)

    private val vm: UploadingProgressViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        initFlowSubscriber()
        startUploadService()
        disableOnBackPress()
    }

    private fun setupListeners() {
        binding.cancelButton.setOnClickListener { handleOnFinish(false) }
    }

    private fun initFlowSubscriber() {
        lifecycleScope.launchWhenStarted {
            vm.viewState.collect { state ->
                updateUi(state)
                if (state.isUploadFinish) handleOnFinish(true)
            }
        }
    }

    private fun updateUi(state: UploadingState) {
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

    private fun handleOnFinish(isComplete: Boolean) {
        vm.cancelUpload()
        findNavController().navigate(R.id.uploadingToSources, Bundle().apply {
            putBoolean(COMPLETE_UPLOAD_KEY, isComplete)
        })
    }

    private fun disableOnBackPress() {
        activity?.onBackPressedDispatcher?.addCallback {
            // do nothing
        }
    }

    companion object {
        const val SELECTIONS_KEY = "selections_bundle_key"
    }

}