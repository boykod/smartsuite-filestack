package ua.motionman.filestack.ui.uploadingprogress

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.filestack.FileLink
import com.filestack.Progress
import com.filestack.StorageOptions
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ua.motionman.filestack.domain.model.Selection
import ua.motionman.filestack.utils.client.ClientProvider

data class UploadingState(
    val currentFile: Int = 0,
    val totalFiles: Int = 0,
    val progress: Int = 0,
    val isUploadFinish: Boolean = false
)

class UploadingProgressViewModel : ViewModel() {

    private val _viewState = MutableStateFlow(UploadingState())
    val viewState = _viewState.asStateFlow()

    private var disposable = mutableListOf<Disposable>()

    private var currentFile = 0

    fun uploadSelections(
        data: Array<Selection>?,
        contentResolver: ContentResolver
    ) {
        if (data.isNullOrEmpty()) return

        updateViewState(_viewState.value.copy(totalFiles = data.size))

        data.forEach { upload(it, contentResolver) }
    }

    private fun upload(
        selection: Selection,
        contentResolver: ContentResolver
    ) {
        val client = ClientProvider.instance.getClient() ?: return

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

        disposable.add(
            upload
                .subscribeOn(Schedulers.io())
                .doOnNext { updateProgress(it) }
                .doOnComplete { proceedOnComplete() }
                .doOnError { Log.e("SourceFragment", "doOnError $it") }
                .subscribe()
        )
    }

    private fun updateProgress(currentProgress: Progress<FileLink>) {
        updateViewState(
            _viewState.value.copy(
                progress = calculateProgress(currentFile, currentProgress.percent),
            )
        )
    }

    private fun calculateProgress(file: Int, percent: Double): Int {
        val size = file + percent
        return (size / _viewState.value.totalFiles * 100).toInt()
    }

    private fun proceedOnComplete() {
        currentFile++
        updateViewState(
            _viewState.value.copy(
                currentFile = currentFile,
                isUploadFinish = currentFile >= _viewState.value.totalFiles
            )
        )
    }

    private fun updateViewState(value: UploadingState) {
        _viewState.value = value
    }

    fun cancelUpload() {
        disposable.forEach { it.dispose() }
        disposable.clear()
    }

}