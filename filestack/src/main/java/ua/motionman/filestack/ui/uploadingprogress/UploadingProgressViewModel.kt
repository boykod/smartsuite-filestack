package ua.motionman.filestack.ui.uploadingprogress

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.filestack.Client
import com.filestack.FileLink
import com.filestack.Progress
import com.filestack.StorageOptions
import io.reactivex.Flowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ua.motionman.filestack.domain.model.Selection
import ua.motionman.filestack.domain.model.UploadResult
import ua.motionman.filestack.utils.client.ClientProvider

data class UiState(
    val currentFile: Int = 0,
    val totalFiles: Int = 0,
    val progress: Int = 0
)

data class UploadEvent(
    val uploadResult: Array<UploadResult> = emptyArray()
)

class UploadingProgressViewModel(
    private val client: Client? = ClientProvider.instance.getClient()
) : ViewModel() {

    private val _viewState = MutableStateFlow(UiState())
    val viewState = _viewState.asStateFlow()

    private val _uploadEvent = Channel<UploadEvent>()
    val uploadEvent = _uploadEvent.receiveAsFlow()

    private val response = mutableListOf<UploadResult>()

    private var currentFile = 0

    fun uploadSelections(
        data: Array<Selection>?,
        contentResolver: ContentResolver
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (data.isNullOrEmpty()) return@launch

        updateViewState(_viewState.value.copy(totalFiles = data.size))

        data.forEach { upload(it, contentResolver) }
    }

    private fun upload(
        selection: Selection,
        contentResolver: ContentResolver
    ) {
        if (client == null) return

        val storeOptions = StorageOptions.Builder()
            .filename(selection.name)
            .mimeType(selection.mimeType)
            .build()

        val upload: Flowable<Progress<FileLink>> = client.uploadAsync(
            contentResolver.openInputStream(Uri.parse(selection.uri)),
            selection.size,
            false,
            storeOptions
        )

        try {
            val file: FileLink = upload
                .doOnNext { updateProgress(it) }
                .doOnError { Log.e("SourceFragment", "doOnError $it") }
                .blockingLast().data

            val responseData = with(file) {
                UploadResult(container, filename, mimeType, size, url, key)
            }
            response.add(responseData)
            proceedOnComplete()
        } catch (e: Exception) {
        }
    }

    private fun updateProgress(currentProgress: Progress<FileLink>) {
        val currentPercent = calculateProgress(currentFile, currentProgress.percent)
        val progress =
            if (currentPercent < _viewState.value.progress) _viewState.value.progress else currentPercent

        updateViewState(_viewState.value.copy(progress = progress))
    }

    private fun calculateProgress(file: Int, percent: Double): Int {
        val size = file + percent
        return (size / _viewState.value.totalFiles * 100).toInt()
    }

    private fun proceedOnComplete() = viewModelScope.launch {
        currentFile++

        if (currentFile >= _viewState.value.totalFiles) {
            _uploadEvent.send(UploadEvent(response.toTypedArray()))
            return@launch
        }

        updateViewState(_viewState.value.copy(currentFile = currentFile))
    }

    private fun updateViewState(value: UiState) {
        _viewState.value = value
    }

    fun cancelUpload() {
        client?.cancelUpload()
        response.clear()
    }

}