package app.lenth.ui.search.dialog

import androidx.compose.runtime.Composable
import lenth.composeapp.generated.resources.Res
import lenth.composeapp.generated.resources.tab_search_dialog_discard_current_place_action_cancel
import lenth.composeapp.generated.resources.tab_search_dialog_discard_current_place_action_discard
import lenth.composeapp.generated.resources.tab_search_dialog_discard_current_place_message
import lenth.composeapp.generated.resources.tab_search_dialog_discard_current_place_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun DiscardCurrentPlaceInput(
    isDiscardCurrentInputAlertDialogVisible: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmDiscard: () -> Unit,
) {
    BasicAlertDialog(
        isAlertDialogVisible = isDiscardCurrentInputAlertDialogVisible,
        title = stringResource(Res.string.tab_search_dialog_discard_current_place_title),
        message = stringResource(Res.string.tab_search_dialog_discard_current_place_message),
        onDismissRequest = onDismissRequest,
        onConfirm = onConfirmDiscard,
        confirmButtonText = stringResource(Res.string.tab_search_dialog_discard_current_place_action_discard),
        dismissButtonText = stringResource(Res.string.tab_search_dialog_discard_current_place_action_cancel),
    )
}
