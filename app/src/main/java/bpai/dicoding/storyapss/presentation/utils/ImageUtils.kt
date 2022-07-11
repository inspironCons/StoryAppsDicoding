package bpai.dicoding.storyapss.presentation.utils

import android.app.Application
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import bpai.dicoding.storyapss.R
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

object ImageUtils {
    private const val FILENAME_FORMAT = "dd-MMM-yyyy"

    private val timeStamp: String = SimpleDateFormat(
        FILENAME_FORMAT,
        Locale.US
    ).format(System.currentTimeMillis())

    fun createFile(application: Application):File{
        val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
            File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
        }

        val outputDirectory = if (
            mediaDir != null && mediaDir.exists()
        ) mediaDir else application.filesDir

        return File(outputDirectory, "$timeStamp.jpg")
    }

    fun Uri.toFile(application: Application):File{
        val contentResolver: ContentResolver = application.contentResolver
        val myFile = createFile(application)

        val inputStream = contentResolver.openInputStream(this) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    /**
     * reduce size nearly 200KB
     * @return File
     */
    fun File.reduceSize(): File? {
        return try {

            // BitmapFactory options to downsize the image
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            o.inSampleSize = 6
            // factor of downsizing the image
            var inputStream = FileInputStream(this)
            //Bitmap selectedBitmap = null;
            BitmapFactory.decodeStream(inputStream, null, o)
            inputStream.close()

            // The new size we want to scale to
            val requiredSize = 75

            // Find the correct scale value. It should be the power of 2.
            var scale = 1
            while (o.outWidth / scale / 2 >= requiredSize &&
                o.outHeight / scale / 2 >= requiredSize
            ) {
                scale *= 2
            }
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            inputStream = FileInputStream(this)
            val selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2)
            inputStream.close()

            // here i override the original image file
            this.createNewFile()
            val outputStream = FileOutputStream(this)
            selectedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            this
        } catch (e: Exception) {
            null
        }
    }
}