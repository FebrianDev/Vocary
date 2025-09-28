package com.febriandev.vocary.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex

@Composable
fun CustomAnimatedModalSheet(
    show: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val visibleState = remember { MutableTransitionState(false) }
    visibleState.targetState = show

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
                .windowInsetsPadding(WindowInsets.systemBars)
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.background,
                    //RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .padding(24.dp)
        ) {
            content()
            Column(
                modifier = Modifier
                    .padding(bottom = 0.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = "Built by febriandev",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    //  fontFamily = FontFamily(Font(R.font.publicsans_medium))
                )
                Text(
                    text = "v1.0.0",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    //  fontFamily = FontFamily(Font(R.font.publicsans_regular)),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
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
