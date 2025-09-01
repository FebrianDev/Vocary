package com.febriandev.vocary.ui.components

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Wallpaper
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.febriandev.vocary.utils.copyText
import com.febriandev.vocary.utils.imageBitmapWithBackground
import com.febriandev.vocary.utils.saveImageToGallery
import com.febriandev.vocary.utils.setAsWallpaper
import com.febriandev.vocary.utils.shareImage
import com.febriandev.vocary.utils.surfaceWithTonalElevation
import com.febriandev.vocary.domain.Vocabulary
import com.febriandev.vocary.ui.vm.VocabularyViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun VocabularyShare(
    vocabulary: Vocabulary,
    capturedImage: ImageBitmap?,
    context: Context,
    activity: Activity,
    showSheet: Boolean,
    vocabularyViewModel: VocabularyViewModel,
    onDismiss: () -> Unit
) {
    if (capturedImage != null) {

        val color =
            surfaceWithTonalElevation(
                MaterialTheme.colorScheme.surface,
                MaterialTheme.colorScheme.surfaceTint,
                4.dp
            ).toArgb()


        val bitmapWithBg = remember(capturedImage, color) {
            imageBitmapWithBackground(capturedImage, color)
        }

        CustomAnimatedModalSheet2("Share", showSheet, onDismiss) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    bitmap = capturedImage,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                )

                FlowRow(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.Center,
                    maxItemsInEachRow = 3 // jumlah item per baris
                ) {
                    ItemShare(Icons.Default.Download, "Save Image") {
                        saveImageToGallery(context, bitmapWithBg)
                    }
                    ItemShare(Icons.Default.Favorite, "Add to\nFavorite") {
                        vocabularyViewModel.addToFavorite(vocabulary.id)
                    }
                    ItemShare(Icons.Default.Share, "Share") {
                        shareImage(activity, bitmapWithBg)
                    }
                    ItemShare(Icons.Default.ContentCopy, "Copy Text") {
                        copyText(context, vocabulary.word)
                    }
                    ItemShare(Icons.Default.Wallpaper, "Set as\nWallpaper") {
                        setAsWallpaper(context, bitmapWithBg)
                    }
                    ItemShare(Icons.Default.Report, "Report") {
                        vocabularyViewModel.addReport(vocabulary.id)
                        onDismiss.invoke()
                    }
                }
            }

        }
    }
}

@Composable
fun ItemShare(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .clickable {
                onClick.invoke()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(MaterialTheme.colorScheme.primary, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}


