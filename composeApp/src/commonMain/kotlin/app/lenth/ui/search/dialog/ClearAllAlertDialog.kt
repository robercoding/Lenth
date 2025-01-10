package app.lenth.ui.search.dialog

import androidx.compose.runtime.Composable

@Composable
fun ClearAllAlertDialog(
    isClearAllAlertDialogVisible: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmClearAll: () -> Unit,
) {
    BasicAlertDialog(
        isAlertDialogVisible = isClearAllAlertDialogVisible,
        title = "Clear all?",
        message = "Do you want to clear all the places you have added?",
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirmClearAll,
        confirmButtonText = "Clear",
        dismissButtonText = "Cancel",
    )
}