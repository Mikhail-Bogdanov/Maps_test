package com.qwertyuiop.presentation.ui.composables.location.mvi

import com.qwertyuiop.core.mviViewModel.MviViewModel
import com.qwertyuiop.presentation.ui.composables.location.mvi.PermissionsEvent.GoToSettingsClicked
import com.qwertyuiop.presentation.ui.composables.location.mvi.PermissionsEvent.HideGoToSettingsDialog
import com.qwertyuiop.presentation.ui.composables.location.mvi.PermissionsEvent.HidePermissionDialog
import com.qwertyuiop.presentation.ui.composables.location.mvi.PermissionsEvent.RequestPermissionClicked
import com.qwertyuiop.presentation.ui.composables.location.mvi.PermissionsEvent.RequestedPermissionGranted
import com.qwertyuiop.presentation.ui.composables.location.mvi.PermissionsEvent.ShowGoToSettingsDialog
import com.qwertyuiop.presentation.ui.composables.location.mvi.PermissionsEvent.ShowPermissionDialog
import com.qwertyuiop.presentation.ui.composables.location.mvi.PermissionsSideEffect.GoToSettings
import com.qwertyuiop.presentation.ui.composables.location.mvi.PermissionsSideEffect.OnPermissionGranted
import com.qwertyuiop.presentation.ui.composables.location.mvi.PermissionsSideEffect.RequestPermission
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce

class PermissionsViewModel : MviViewModel<PermissionsState, PermissionsSideEffect, PermissionsEvent>(
    initialState = PermissionsState()
) {
    override fun dispatch(event: PermissionsEvent) {
        when (event) {
            GoToSettingsClicked -> goToSettingsClicked()
            RequestPermissionClicked -> requestPermissionClicked()
            RequestedPermissionGranted -> requestedPermissionGranted()
            HideGoToSettingsDialog -> hideGoToSettingsDialog()
            HidePermissionDialog -> hidePermissionDialog()
            ShowGoToSettingsDialog -> showGoToSettingsDialog()
            ShowPermissionDialog -> showPermissionDialog()
        }
    }

    private fun hideGoToSettingsDialog() = intent {
        reduce { state.copy(isGoToSettingsDialogVisible = false) }
    }

    private fun hidePermissionDialog() = intent {
        reduce { state.copy(isPermissionDialogVisible = false) }
    }

    private fun showGoToSettingsDialog() = intent {
        reduce { state.copy(isGoToSettingsDialogVisible = true) }
    }

    private fun showPermissionDialog() = intent {
        reduce { state.copy(isPermissionDialogVisible = true) }
    }

    private fun requestedPermissionGranted() = intent {
        postSideEffect(OnPermissionGranted)
    }

    private fun requestPermissionClicked() = intent {
        reduce { state.copy(isPermissionDialogVisible = false) }
        postSideEffect(RequestPermission)
    }

    private fun goToSettingsClicked() = intent {
        postSideEffect(GoToSettings)
        reduce { state.copy(isGoToSettingsDialogVisible = false) }
    }
}