package app.lenth.ui.previews

import androidx.compose.runtime.Composable
import app.lenth.ui.components.LenthPrimaryButton
import app.lenth.ui.theme.ActionBlue
import app.lenth.ui.theme.OnActionBlue

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun LenthPrimaryButtonPreview() {
    LenthPrimaryButton(
        text = "Start searching",
        textColor = OnActionBlue,
        backgroundColor = ActionBlue,
        onClick = {},
    )
}