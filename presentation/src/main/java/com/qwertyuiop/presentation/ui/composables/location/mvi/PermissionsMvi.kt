package com.qwertyuiop.presentation.ui.composables.location.mvi

data class PermissionsState(
    val isPermissionDialogVisible: Boolean = false,
    val isGoToSettingsDialogVisible: Boolean = false
)

sealed interface PermissionsEvent {
    data object RequestPermissionClicked : PermissionsEvent
    data object GoToSettingsClicked : PermissionsEvent
    data object RequestedPermissionGranted : PermissionsEvent
    data object ShowPermissionDialog : PermissionsEvent
    data object HidePermissionDialog : PermissionsEvent
    data object ShowGoToSettingsDialog : PermissionsEvent
    data object HideGoToSettingsDialog : PermissionsEvent
}

sealed interface PermissionsSideEffect {
    data object RequestPermission : PermissionsSideEffect
    data object GoToSettings : PermissionsSideEffect
    data object OnPermissionGranted : PermissionsSideEffect
}
