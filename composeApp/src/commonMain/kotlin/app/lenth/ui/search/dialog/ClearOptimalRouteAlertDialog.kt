package app.lenth.ui.search.dialog

import androidx.compose.runtime.Composable
import lenth.composeapp.generated.resources.Res
import lenth.composeapp.generated.resources.sheet_dialog_clear_optimal_route_action_cancel
import lenth.composeapp.generated.resources.sheet_dialog_clear_optimal_route_action_delete
import lenth.composeapp.generated.resources.sheet_dialog_clear_optimal_route_message
import lenth.composeapp.generated.resources.sheet_dialog_clear_optimal_route_title
import lenth.composeapp.generated.resources.sheet_optimal_route_title
import lenth.composeapp.generated.resources.tab_history_dialog_clear_all_optimal_routes_action_cancel
import lenth.composeapp.generated.resources.tab_history_dialog_clear_all_optimal_routes_action_clear
import lenth.composeapp.generated.resources.tab_history_dialog_clear_all_optimal_routes_message
import org.jetbrains.compose.resources.stringResource

@Composable
fun ClearOptimalRouteAlertDialog(
    isDeleteOptimalRouteVisible: Boolean,
    onDismissDelete: () -> Unit,
    onConfirmDelete: () -> Unit,
) {
    BasicAlertDialog(
        isAlertDialogVisible = isDeleteOptimalRouteVisible,
        title = stringResource(Res.string.sheet_dialog_clear_optimal_route_title),
        message = stringResource(Res.string.sheet_dialog_clear_optimal_route_message),
        onDismissRequest = onDismissDelete,
        onConfirm = onConfirmDelete,
        confirmButtonText = stringResource(Res.string.sheet_dialog_clear_optimal_route_action_delete),
        dismissButtonText = stringResource(Res.string.sheet_dialog_clear_optimal_route_action_cancel),
    )
}