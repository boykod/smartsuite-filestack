package ua.motionman.filestack.utils.source

import com.filestack.Sources
import ua.motionman.filestack.R
import ua.motionman.filestack.domain.model.SourceModel

val localSources = listOf(
    SourceModel.Source(Sources.DEVICE, R.string.source_device),
    SourceModel.Source(Sources.CAMERA, R.string.source_camera)
)

val localSourceData = listOf(SourceModel.Header(R.string.source_local)) + localSources

val remoteSources = listOf(
    SourceModel.Source(Sources.DEVICE, R.string.source_facebook),
    SourceModel.Source(Sources.CAMERA, R.string.source_instagram),
    SourceModel.Source(Sources.CAMERA, R.string.source_google_drive),
    SourceModel.Source(Sources.CAMERA, R.string.source_dropbox),
    SourceModel.Source(Sources.CAMERA, R.string.source_box),
    SourceModel.Source(Sources.CAMERA, R.string.source_github),
    SourceModel.Source(Sources.CAMERA, R.string.source_gmail),
    SourceModel.Source(Sources.CAMERA, R.string.source_google_photos),
    SourceModel.Source(Sources.CAMERA, R.string.source_onedrive),
    SourceModel.Source(Sources.CAMERA, R.string.source_amazon_drive)
)

val remoteSourceData = listOf(SourceModel.Header(R.string.source_remote)) + remoteSources

val allSourceData = localSourceData + remoteSourceData