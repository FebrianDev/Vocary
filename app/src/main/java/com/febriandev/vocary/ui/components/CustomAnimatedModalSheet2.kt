package com.febriandev.vocary.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun CustomAnimatedModalSheet2(
    title:String,
    show: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val visibleState = remember { MutableTransitionState(false) }
    visibleState.targetState = show

    BackHandler(show) {
        onDismiss.invoke()
    }

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
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(
//                    MaterialTheme.colorScheme.surface,
//                    //RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
//                )

        // ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            //     shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp,
            // border = if(shouldCaptureScreenshot) BorderStroke(1.dp, Color.White) else null
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                TitleTopBar(title, Icons.Default.Close) { onDismiss.invoke() }
                content()
            }
        }
    }
    //   }
}