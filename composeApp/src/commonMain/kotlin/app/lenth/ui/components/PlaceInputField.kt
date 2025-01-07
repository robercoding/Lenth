package app.lenth.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PlaceInputField(
    modifier: Modifier = Modifier,
    text: String,
    isSetByAutocomplete: Boolean,
    hint: String,
    onQueryChanged: (String) -> Unit,
    onClickDelete: () -> Unit,
    onFocused: () -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }
    val borderColor by animateColorAsState(if (isFocused) Color.White else Color.Transparent)
    var internalText by remember { mutableStateOf(text) }
    LaunchedEffect(isSetByAutocomplete) {
        // Got priority from autocomplete
        if(isSetByAutocomplete) {
            internalText = text
            return@LaunchedEffect
        }

        // Cleared all
        if(!isSetByAutocomplete && text.isEmpty()) {
            internalText = ""
            return@LaunchedEffect
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.DarkGray, RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            value = internalText,
            onValueChange = {
                internalText = it
                onQueryChanged(it)
            },
            placeholder = { Text(hint, color = Color.Gray) },
            modifier = Modifier
                .weight(1f)
                .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                    if (focusState.isFocused) {
                        onFocused()
                    }
                },
            trailingIcon = {
                androidx.compose.animation.AnimatedVisibility(
                    visible = isSetByAutocomplete,
                    enter = fadeIn() + scaleIn(),
                    exit = fadeOut(),
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Clear",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp).clickable(onClick = onClickDelete),
                    )
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.White,
                backgroundColor = Color.Transparent,
                cursorColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                placeholderColor = Color.Gray,
            ),
        )
    }
}