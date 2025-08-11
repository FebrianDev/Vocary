package com.febriandev.vocary.utils

import android.graphics.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.createBitmap
//import com.febrian.vocery.ui.theme.baseScreenColor

class ScreenshotController {
    var onCapture: (() -> Unit)? = null
    fun capture() {
        onCapture?.invoke()
    }
}


@Composable
fun ScreenshotBox(
    modifier: Modifier,
    controller: ScreenshotController,
    onBitmapCaptured: (ImageBitmap) -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val composeView = remember { ComposeView(context) }

    AndroidView(factory = {
        composeView.apply {
            setContent {
                MaterialTheme {
                    Box(
                      modifier = modifier
                    ) {
                        content()
                    }
                }
            }
        }
    })

    DisposableEffect(Unit) {
        controller.onCapture = {
            composeView.post {
                val bitmap = createBitmap(composeView.width, composeView.height)
                val canvas = Canvas(bitmap)
                composeView.draw(canvas)

                onBitmapCaptured(bitmap.asImageBitmap())
            }
        }
        onDispose {
            controller.onCapture = null
        }
    }
}
