package com.febrian.vocery.utils

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.febriandev.vocary.BuildConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.microsoft.cognitiveservices.speech.CancellationReason
import com.microsoft.cognitiveservices.speech.ResultReason
import com.microsoft.cognitiveservices.speech.SpeechConfig
import com.microsoft.cognitiveservices.speech.SpeechSynthesisCancellationDetails
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.UUID

suspend fun downloadAndSaveAudio(context: Context, url: String, fileName: String): File {
    val file = File(context.filesDir, fileName)
    if (!file.exists()) {
        withContext(Dispatchers.IO) {
            val connection = URL(url).openConnection() as HttpURLConnection
            connection.connect()
            file.outputStream().use { output ->
                connection.inputStream.use { input ->
                    input.copyTo(output)
                }
            }
            connection.disconnect()
        }
    }
    return file
}

//fun playAudioFromFile(file: File) {
//    MediaPlayer().apply {
//        setDataSource(file.absolutePath)
//        prepare()
//        start()
//        setOnCompletionListener {
//            release()
//        }
//    }
//}

fun playAudioFromFile(file: File) {
    try {
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(file.absolutePath)
        mediaPlayer.prepare()
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.release()
        }
    } catch (e: IOException) {
        e.printStackTrace()
        Log.e("MediaPlayer", "Gagal memutar audio: ${e.message}")
    } catch (e: IllegalArgumentException) {
        e.printStackTrace()
        Log.e("MediaPlayer", "Argumen tidak valid: ${e.message}")
    } catch (e: IllegalStateException) {
        e.printStackTrace()
        Log.e("MediaPlayer", "State tidak valid: ${e.message}")
    }
}


fun Context.showMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun speakText(text: String) {
    val speechConfig = SpeechConfig.fromSubscription(BuildConfig.API_TEXT_TO_SPEECH, "southeastasia")

    // Optional: atur suara (voice)
    speechConfig.speechSynthesisVoiceName = "en-US-AvaMultilingualNeural"

    val synthesizer = SpeechSynthesizer(speechConfig)

    val result = synthesizer.SpeakTextAsync(text).get()

    if (result.reason == ResultReason.SynthesizingAudioCompleted) {
        println("Speech synthesized to speaker for text: [$text]")
    } else if (result.reason == ResultReason.Canceled) {
        val cancellationDetails = SpeechSynthesisCancellationDetails.fromResult(result)
        println("Canceled: Reason=${cancellationDetails.reason}")

        if (cancellationDetails.reason == CancellationReason.Error) {
            println("Error details: ${cancellationDetails.errorDetails}")
        }
    }

    synthesizer.close()
}

fun shareImage(context: Context, bitmap: Bitmap) {
    val cachePath = File(context.cacheDir, "images")
    cachePath.mkdirs()
    val file = File(cachePath, "shared_image.png")
    FileOutputStream(file).use {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
    }

    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider", // pastikan sesuai dengan di manifest
        file
    )

    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, uri)
        type = "image/png"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
}

fun saveImageToGallery(context: Context, bitmap: Bitmap, fileName: String = "screenshot_${System.currentTimeMillis()}.png") {
    val fos: OutputStream?
    val imageUri: Uri?

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        imageUri = uri
        fos = uri?.let { resolver.openOutputStream(it) }
    } else {
        val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, fileName)
        fos = FileOutputStream(image)
        imageUri = Uri.fromFile(image)
    }

    fos?.use {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        Toast.makeText(context, "Image saved!", Toast.LENGTH_SHORT).show()
    }
}

fun generateRandomId(): String {
    return UUID.randomUUID().toString()
}