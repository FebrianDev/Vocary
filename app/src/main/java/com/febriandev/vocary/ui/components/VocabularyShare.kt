package com.febriandev.vocary.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyShare(
    showSheet: Boolean,
    capturedImage: ImageBitmap?,
    bottomSheetState: SheetState,
    onDismiss: () -> Unit
) {
    if (capturedImage != null) {
        val visibleState = remember { MutableTransitionState(false) }
        visibleState.targetState = showSheet

        AnimatedVisibility(
            visibleState = visibleState,
            enter = slideInVertically(
                animationSpec = tween(durationMillis = 400),
                initialOffsetY = { fullHeight -> fullHeight }
            ) + fadeIn(animationSpec = tween(400)),
            exit = slideOutVertically(
                animationSpec = tween(durationMillis = 400),
                targetOffsetY = { fullHeight -> fullHeight }
            ) + fadeOut(animationSpec = tween(400)),
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .zIndex(1f) // pastikan di atas layer lainnya
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.background,
                        //RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 64.dp)
            ) {
                Column {
                    Image(
                        bitmap = capturedImage, contentDescription = null, modifier = Modifier
                            .fillMaxWidth()
                            .height(640.dp)
                    )

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

