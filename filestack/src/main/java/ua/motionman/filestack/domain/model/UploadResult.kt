package ua.motionman.filestack.domain.model

import java.io.Serializable

data class UploadResult(
    val container: String,
    val filename: String,
    val mimeType: String,
    val size: Int,
    val url: String,
    val key: String
) : Serializable
