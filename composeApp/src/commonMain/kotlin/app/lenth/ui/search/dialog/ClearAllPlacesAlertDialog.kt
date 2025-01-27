package app.lenth.ui.search.dialog

import androidx.compose.runtime.Composable
import lenth.composeapp.generated.resources.Res
import lenth.composeapp.generated.resources.tab_search_dialog_clear_all_places_action_cancel
import lenth.composeapp.generated.resources.tab_search_dialog_clear_all_places_action_clear
import lenth.composeapp.generated.resources.tab_search_dialog_clear_all_places_message
import lenth.composeapp.generated.resources.tab_search_dialog_clear_all_places_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun ClearAllPlacesAlertDialog(
    isClearAllAlertDialogVisible: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmClearAll: () -> Unit,
) {
    BasicAlertDialog(
        isAlertDialogVisible = isClearAllAlertDialogVisible,
        title = stringResource(Res.string.tab_search_dialog_clear_all_places_title),
        message = stringResource(Res.string.tab_search_dialog_clear_all_places_message),
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirmClearAll,
        confirmButtonText = stringResource(Res.string.tab_search_dialog_clear_all_places_action_clear),
        dismissButtonText = stringResource(Res.string.tab_search_dialog_clear_all_places_action_cancel),
    )
}