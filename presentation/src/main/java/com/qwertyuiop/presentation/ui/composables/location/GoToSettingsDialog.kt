package com.qwertyuiop.presentation.ui.composables.location

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.qwertyuiop.presentation.ui.composables.location.mvi.PermissionsEvent
import com.qwertyuiop.presentation.ui.composables.location.mvi.PermissionsEvent.HideGoToSettingsDialog

@Composable
fun GoToSettingsDialog(
    onEvent: (PermissionsEvent) -> Unit,
    content: @Composable () -> Unit
) = Dialog(
    onDismissRequest = { onEvent(HideGoToSettingsDialog) },
    properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
    content = content
)