package app.lenth.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import app.lenth.ui.utils.ButtonHeight
import app.lenth.ui.utils.ButtonRoundedCornerShape

@Composable
fun DeleteIconButton(
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(ButtonHeight)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(ButtonRoundedCornerShape))
            .clip(RoundedCornerShape(ButtonRoundedCornerShape))
            .background(MaterialTheme.colorScheme.errorContainer)
            .clickable { onClick() },
        contentAlignment = androidx.compose.ui.Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Outlined.Delete,
            contentDescription = Icons.Outlined.Delete.name,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onErrorContainer,
        )
    }
}