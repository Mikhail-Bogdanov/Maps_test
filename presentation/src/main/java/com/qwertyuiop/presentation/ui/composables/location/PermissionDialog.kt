package com.qwertyuiop.presentation.ui.composables.location

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.qwertyuiop.presentation.ui.composables.location.mvi.PermissionsEvent
import com.qwertyuiop.presentation.ui.composables.location.mvi.PermissionsEvent.HidePermissionDialog

@Composable
fun PermissionDialog(
    onEvent: (PermissionsEvent) -> Unit,
    content: @Composable () -> Unit
) = Dialog(
    onDismissRequest = { onEvent(HidePermissionDialog) },
    properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
    content = content
)