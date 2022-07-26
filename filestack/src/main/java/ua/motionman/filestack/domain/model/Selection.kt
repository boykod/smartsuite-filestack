package ua.motionman.filestack.domain.model

import java.io.Serializable

data class Selection(
    val provider: String,
    val uri: String,
    val size: Int,
    val mimeType: String,
    val name: String
) : Serializable
