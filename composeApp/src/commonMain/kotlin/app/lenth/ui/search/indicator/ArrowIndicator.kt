package app.lenth.ui.search.indicator

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Logger

@Composable
fun ArrowIndicator(lazyColumnState: LazyListState) {

    var alreadyScrolled by rememberSaveable { mutableStateOf(false) }
    val shouldShowArrow = lazyColumnState.canScrollForward && !alreadyScrolled

    LaunchedEffect(lazyColumnState.firstVisibleItemScrollOffset) {
        if (alreadyScrolled) {
            return@LaunchedEffect
        }
        Logger.i("First visible item index: ${lazyColumnState.firstVisibleItemIndex}")
        if (lazyColumnState.firstVisibleItemScrollOffset > 20) {
            alreadyScrolled = true
        }
    }

    androidx.compose.animation.AnimatedVisibility(
        modifier = Modifier.animateContentSize(),
        visible = shouldShowArrow,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.CenterVertically),
        exit = fadeOut() + shrinkVertically(),
    ) {
        Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            val infiniteTransition = rememberInfiniteTransition()
            val arrowOffset by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 10f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 450, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse,
                ),
            )

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Scroll down",
                tint = Color.White,
                modifier = Modifier
                    .offset(y = arrowOffset.dp)
                    .size(24.dp),
            )
        }
    }

}