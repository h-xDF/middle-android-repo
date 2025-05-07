package com.example.androidpracticumcustomview.ui.theme

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import kotlinx.coroutines.launch

@Composable
fun CustomContainerCompose(
    firstChild: @Composable (() -> Unit)?,
    secondChild: @Composable (() -> Unit)?,
    durationAlphaAnimated: Int = 2000,
    durationTransformAnimated: Int = 5000
) {
    // Блок создания и инициализации переменных
    val alpha = remember { Animatable(0f) }
    val firstOffset = remember { Animatable(0f) }
    val secondOffset = remember { Animatable(0f) }
    var parentHeight = 0

    // Блок активации анимации при первом запуске
    LaunchedEffect(Unit) {
        if (firstChild != null) {
            launch {
                alpha.animateTo(1f, animationSpec = tween(durationMillis = durationAlphaAnimated))
            }
            launch {
                firstOffset.animateTo(
                    -((parentHeight / 4).toFloat()),
                    animationSpec = tween(durationMillis = durationTransformAnimated)
                )
            }
        }

        if (secondChild != null) {
            launch {
                alpha.animateTo(1f, animationSpec = tween(durationMillis = durationAlphaAnimated))
            }
            launch {
                secondOffset.animateTo(
                    ((parentHeight / 4).toFloat()),
                    animationSpec = tween(durationMillis = durationTransformAnimated)
                )
            }
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .onSizeChanged {
                parentHeight = it.height
            }
    ) {
        if (firstChild != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .graphicsLayer {
                        this.alpha = alpha.value
                        translationY = firstOffset.value
                    }
            ) {
                firstChild()
            }
        }

        if (secondChild != null) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .graphicsLayer {
                        this.alpha = alpha.value
                        translationY = secondOffset.value
                    }
            ) {
                secondChild()
            }
        }
    }
}