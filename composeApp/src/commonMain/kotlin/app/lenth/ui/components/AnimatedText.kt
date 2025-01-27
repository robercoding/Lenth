package app.lenth.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.lenth.ui.search.ANIMATION_TIME

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AnimatedText(
    text: String,
    modifier: Modifier = Modifier,
    component: @Composable (String) -> Unit
) {
    AnimatedContent(
        modifier = modifier,
        targetState = text,
        transitionSpec = {
            fadeIn(animationSpec = tween(ANIMATION_TIME)) togetherWith fadeOut(animationSpec = tween(ANIMATION_TIME))
        },
    ) { targetText ->
        Box {
            component(targetText)
        }
    }
}