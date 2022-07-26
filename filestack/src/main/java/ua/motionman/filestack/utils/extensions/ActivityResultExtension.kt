package ua.motionman.filestack.utils.extensions

import android.content.ClipData
import android.content.Context
import androidx.activity.result.ActivityResult
import ua.motionman.filestack.domain.model.Selection

fun ActivityResult.toSelection(context: Context): Array<Selection> {
    val clipData: ClipData? = this.data?.clipData
    val data = this.data?.dataString

    if (clipData != null) {
        return List(clipData.itemCount) { 0 }.mapIndexed { index, _ ->
            clipData.getItemAt(index).uri.toString().processUri(context)
        }.filterNotNull().toTypedArray()
    }

    val selection = data?.processUri(context)
    if (selection != null) {
        return arrayOf(selection)
    }

    return emptyArray()
}