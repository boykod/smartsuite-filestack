package ua.motionman.filestack.utils.extensions

import androidx.camera.core.CameraSelector

fun CameraSelector.switchSelector(): CameraSelector {
    return when (this) {
        CameraSelector.DEFAULT_BACK_CAMERA -> CameraSelector.DEFAULT_FRONT_CAMERA
        CameraSelector.DEFAULT_FRONT_CAMERA -> CameraSelector.DEFAULT_BACK_CAMERA
        else -> CameraSelector.DEFAULT_BACK_CAMERA
    }
}