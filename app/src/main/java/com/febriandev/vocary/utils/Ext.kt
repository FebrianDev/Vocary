package com.febriandev.vocary.utils

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.unit.Dp
import androidx.core.content.FileProvider
import com.febriandev.vocary.BuildConfig
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
import java.util.Locale
import java.util.UUID
import kotlin.math.ln


@SuppressLint("HardwareIds")
fun getAppId(context: Context): String {
    var androidId =
        Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    if (androidId == null) androidId = ""
    return androidId + "-" + UUID.randomUUID().toString()
}

fun getDeviceName(): String {
    val manufacturer = Build.MANUFACTURER
    val model = Build.MODEL
    return if (model.lowercase(Locale.getDefault())
            .startsWith(manufacturer.lowercase(Locale.getDefault()))
    ) {
        capitalize(model)
    } else {
        capitalize(manufacturer) + " " + model
    }
}

private fun capitalize(s: String?): String {
    if (s.isNullOrEmpty()) {
        return ""
    }
    val first = s[0]
    return if (Character.isUpperCase(first)) {
        s
    } else {
        first.uppercaseChar().toString() + s.substring(1)
    }
}


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
    val speechConfig =
        SpeechConfig.fromSubscription(BuildConfig.API_TEXT_TO_SPEECH, "southeastasia")

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

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_STREAM, uri)
        type = "image/png"
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    val chooser = Intent.createChooser(shareIntent, "Share via").apply {
        // Tambahkan flag disini
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    context.startActivity(Intent.createChooser(chooser, "Share via"))
}

fun saveImageToGallery(
    context: Context,
    bitmap: Bitmap,
    fileName: String = "screenshot_${System.currentTimeMillis()}.png"
) {
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
        val imagesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val image = File(imagesDir, fileName)
        fos = FileOutputStream(image)
        imageUri = Uri.fromFile(image)
    }

    fos?.use {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        Toast.makeText(context, "Image saved!", Toast.LENGTH_SHORT).show()
    }
}

fun copyText(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Copied Text", text)
    clipboard.setPrimaryClip(clip)

    context.showMessage("Copied to clipboard")
}


fun setAsWallpaper(context: Context, capturedImage: Bitmap?) {
    if (capturedImage == null) {
        context.showMessage("No image to set as wallpaper")
        return
    }

    try {
        val wallpaperManager = WallpaperManager.getInstance(context)

        wallpaperManager.setBitmap(capturedImage)

        // wallpaperManager.setBitmap(scaledBitmap)
        context.showMessage("Wallpaper set successfully")
    } catch (e: Exception) {
        e.printStackTrace()
        context.showMessage("Failed to set wallpaper: ${e.message}")
    }
}

fun imageBitmapWithBackground(image: ImageBitmap, backgroundColor: Int): Bitmap {
    val original = image.asAndroidBitmap()

    val result = Bitmap.createBitmap(original.width, original.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(result)

    // Gambar background
    val paint = Paint()
    paint.color = backgroundColor
    canvas.drawRect(0f, 0f, original.width.toFloat(), original.height.toFloat(), paint)

    // Gambar image di atas background
    canvas.drawBitmap(original, 0f, 0f, null)

    return result
}


fun surfaceWithTonalElevation(
    surface: Color,
    surfaceTint: Color,
    elevation: Dp
): Color {
    if (elevation.value <= 0f) return surface

    val alpha = ((4.5f * ln((elevation.value + 1).toDouble()) + 2f) / 100f).toFloat()
    return compositeColors(surfaceTint.copy(alpha = alpha), surface)
}

fun compositeColors(foreground: Color, background: Color): Color {
    val fgAlpha = foreground.alpha
    val bgAlpha = background.alpha
    val a = fgAlpha + bgAlpha * (1 - fgAlpha)

    val r = (foreground.red * fgAlpha + background.red * bgAlpha * (1 - fgAlpha)) / a
    val g = (foreground.green * fgAlpha + background.green * bgAlpha * (1 - fgAlpha)) / a
    val b = (foreground.blue * fgAlpha + background.blue * bgAlpha * (1 - fgAlpha)) / a

    return Color(r, g, b, a)
}


fun generateRandomId(): String {
    return UUID.randomUUID().toString()
}