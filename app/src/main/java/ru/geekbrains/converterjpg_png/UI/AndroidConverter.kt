package ru.geekbrains.converterjpg_png.UI

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import moxy.MvpAppCompatActivity
import ru.geekbrains.converterjpg_png.MVP.model.IDataConverter
import ru.geekbrains.converterjpg_png.MVP.model.Image
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.RuntimeException
import java.util.*

class AndroidConverter(var context: Context): IDataConverter {

    override fun convert(image: Image?): Single<ByteArray> {
        return Single.create<ByteArray> { emitter ->
            val result = convertToPng(convertByteArrayToBitmap(image?.byteArray))
            if (result != null && result is ByteArray) {
                emitter.onSuccess(result)
            } else {
                emitter.onError(RuntimeException("Fail"))
            }
        }.subscribeOn(Schedulers.computation())
    }

    fun convertToPng(imageBitmap: Bitmap?): ByteArray? {
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val out = ByteArrayOutputStream()
        try {
            imageBitmap?.compress(Bitmap.CompressFormat.PNG, 100, out) //100-best quality
            out.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return out.toByteArray()
    }

    fun saveImageToFile(image: Image?, context: Context) {
        val wrapper = ContextWrapper(context)
        val file = wrapper.getDir("PNG", MvpAppCompatActivity.MODE_PRIVATE)
        val savingFile = File(file, "${UUID.randomUUID()}.png")
        val fos = FileOutputStream(savingFile)
        fos.write(image?.byteArray)
        fos.flush()
        fos.close()
        println("PATH: " + savingFile.absolutePath)
    }

    override fun saveImage(image: Image?) = Completable.create { emitter ->
        try {
            saveImageToFile(image, context)
            emitter.onComplete()
        } catch (e: Throwable){
            emitter.onError(java.lang.Exception("Saving error"))
        }
    }.subscribeOn(Schedulers.io())

    fun convertUriToBitmap(uriImg: Uri?, context: Context): Bitmap? {
        var imageBitmap: Bitmap? = null
        uriImg?.let {
            val source: ImageDecoder.Source =
                ImageDecoder.createSource(context.contentResolver, it)
            imageBitmap = ImageDecoder.decodeBitmap(source)
        }
        return imageBitmap
    }

    fun convertBitmapToByteArray(bitmap: Bitmap?): ByteArray {
        val out = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG,100,out)
        out.close()
        return out.toByteArray()
    }

    fun convertByteArrayToBitmap(byteArr: ByteArray?): Bitmap? {
        return byteArr?.let { BitmapFactory.decodeByteArray(it, 0, it.size)}
    }
}