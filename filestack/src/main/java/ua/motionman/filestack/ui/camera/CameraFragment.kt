package ua.motionman.filestack.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ua.motionman.filestack.R
import ua.motionman.filestack.databinding.CameraFragmentBinding
import ua.motionman.filestack.ui.uploadingprogress.UploadingProgressFragment.Companion.SELECTIONS_KEY
import ua.motionman.filestack.utils.camera.CameraProvider
import ua.motionman.filestack.utils.delegate.viewBinding
import ua.motionman.filestack.utils.extensions.processUri

class CameraFragment : Fragment(R.layout.camera_fragment) {

    private val binding by viewBinding(CameraFragmentBinding::bind)

    private val cameraProvider = CameraProvider()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkForPermission()
        setupListeners()
    }

    private fun checkForPermission() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            permissionLauncher.launch(REQUIRED_PERMISSIONS)
        }
    }

    private fun setupListeners() {
        with(binding.cameraControlLayout) {
            takePhotoButton.setOnClickListener { takePhoto() }
            switchCameraSelector.setOnClickListener {
                cameraProvider.switchSelector()
                startCamera()
            }
            cancelShotButton.setOnClickListener { findNavController().navigateUp() }
        }

        with(binding.imagePreviewControlLayout) {
            retakePhotoButton.setOnClickListener {
                binding.cameraContainer.visibility = VISIBLE
                binding.previewContainer.visibility = GONE
                startCamera()
            }
            usePhotoButton.setOnClickListener {
                val capture = cameraProvider.getImageUri().toString()
                val selection = capture.processUri(requireContext())

                findNavController().navigate(R.id.cameraToUpload, Bundle().apply {
                    putSerializable(SELECTIONS_KEY, arrayOf(selection))
                })
            }
        }
    }

    private fun startCamera() {
        cameraProvider.startCamera(
            requireContext(),
            this,
            binding.cameraPreviewView.surfaceProvider
        )
    }

    private fun takePhoto() {
        cameraProvider.takePhoto(
            requireContext(),
            onError = {},
            onImageSaved = {
                with(binding) {
                    cameraContainer.visibility = GONE
                    previewContainer.visibility = VISIBLE
                    photoPreview.setImageURI(it.savedUri)
                }
            }
        )
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireContext(), it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            findNavController().navigateUp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraProvider.onDestroy()
    }

    companion object {
        private val REQUIRED_PERMISSIONS =
            mutableListOf(Manifest.permission.CAMERA).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}