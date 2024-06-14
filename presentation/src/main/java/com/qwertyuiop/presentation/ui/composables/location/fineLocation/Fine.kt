package com.qwertyuiop.presentation.ui.composables.location.fineLocation

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.qwertyuiop.core.navigation.navigateClear
import com.qwertyuiop.presentation.ui.composables.destinations.MapDestination
import com.qwertyuiop.presentation.ui.composables.location.mvi.PermissionsEvent.RequestedPermissionGranted
import com.qwertyuiop.presentation.ui.composables.location.mvi.PermissionsEvent.ShowGoToSettingsDialog
import com.qwertyuiop.presentation.ui.composables.location.mvi.PermissionsEvent.ShowPermissionDialog
import com.qwertyuiop.presentation.ui.composables.location.mvi.PermissionsSideEffect.GoToSettings
import com.qwertyuiop.presentation.ui.composables.location.mvi.PermissionsSideEffect.OnPermissionGranted
import com.qwertyuiop.presentation.ui.composables.location.mvi.PermissionsSideEffect.RequestPermission
import com.qwertyuiop.presentation.ui.composables.location.mvi.PermissionsViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalPermissionsApi::class)
@Destination
@Composable
fun Fine(
    navigator: DestinationsNavigator,
    fromSettings: Boolean = false
) {
    val context = LocalContext.current

    val viewModel = koinViewModel<PermissionsViewModel>()
    val state = viewModel.collectAsState().value

    var fineGranted by remember {
        mutableStateOf(false)
    }

    val finePermission = rememberPermissionState(ACCESS_FINE_LOCATION) { result ->
        fineGranted = result
    }

    LaunchedEffect(Unit) {
        if (!fineGranted) viewModel.dispatch(ShowPermissionDialog)
    }

    //отслеживание можно ли запросить пермишн
    LaunchedEffect(finePermission.status.shouldShowRationale) {
        when (finePermission.status.shouldShowRationale) {
            true -> viewModel.dispatch(ShowPermissionDialog)
            false -> viewModel.dispatch(ShowGoToSettingsDialog)
        }
    }

    //отслеживание дали ли пермишн из настроек
    LifecycleResumeEffect(Unit) {
        val isGranted = finePermission.status.isGranted

        when {
            isGranted -> viewModel.dispatch(RequestedPermissionGranted)
            !isGranted && finePermission.status.shouldShowRationale -> viewModel.dispatch(
                ShowPermissionDialog
            )

            !isGranted && !finePermission.status.shouldShowRationale -> viewModel.dispatch(
                ShowGoToSettingsDialog
            )
        }

        onPauseOrDispose {}//do nothing
    }

    //отслеживание есть ли пермишн
    LaunchedEffect(fineGranted) {
        if (fineGranted) {
            viewModel.dispatch(RequestedPermissionGranted)
            return@LaunchedEffect
        }

        when {
            finePermission.status.shouldShowRationale -> {
                viewModel.dispatch(ShowPermissionDialog)
            }

            !finePermission.status.shouldShowRationale -> {
                viewModel.dispatch(ShowGoToSettingsDialog)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.isGoToSettingsDialogVisible) FineGoToSettingsDialog(viewModel::dispatch)
        if (state.isPermissionDialogVisible) FinePermissionDialog(viewModel::dispatch)
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            GoToSettings -> context.startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null)
                )
            )

            RequestPermission -> finePermission.launchPermissionRequest()
            OnPermissionGranted -> {
                when (fromSettings) {
                    true -> navigator.popBackStack()
                    false -> navigator.navigateClear(MapDestination)
                }
            }
        }
    }
}