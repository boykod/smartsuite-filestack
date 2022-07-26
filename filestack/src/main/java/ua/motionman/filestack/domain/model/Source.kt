package ua.motionman.filestack.domain.model

import androidx.annotation.StringRes

sealed class SourceModel {
    data class Header(@StringRes val section: Int) : SourceModel()

    data class Source(val type: String, @StringRes val name: Int) : SourceModel()
}
