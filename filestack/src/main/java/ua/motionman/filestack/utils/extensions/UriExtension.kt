package ua.motionman.filestack.utils.extensions

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.filestack.Sources
import ua.motionman.filestack.domain.model.Selection

fun String.processUri(context: Context): Selection? {
    val uri = Uri.parse(this)

    val cursor = context.contentResolver.query(
        uri,
        null,
        null,
        null,
        null
    ) ?: return null

    cursor.moveToFirst()

    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

    val fileName = cursor.getString(nameIndex)
    val fileSize = cursor.getInt(sizeIndex)
    val mimeType = context.contentResolver.getType(uri) ?: ""

    cursor.close()
    return Selection(Sources.DEVICE, this, fileSize, mimeType, fileName)
}