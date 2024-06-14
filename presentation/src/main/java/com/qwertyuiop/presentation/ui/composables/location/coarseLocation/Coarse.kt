package com.qwertyuiop.presentation.ui.composables.location.coarseLocation

import android.Manifest.permission.ACCESS_COARSE_LOCATION
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
import com.qwertyuiop.presentation.ui.composables.destinations.FineDestination
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
fun Coarse(
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current

    val viewModel = koinViewModel<PermissionsViewModel>()
    val state = viewModel.collectAsState().value

    var coarseGranted by remember {
        mutableStateOf(false)
    }

    val coarsePermission = rememberPermissionState(ACCESS_COARSE_LOCATION) { result ->
        coarseGranted = result
    }

    LaunchedEffect(Unit) {
        if (!coarseGranted) viewModel.dispatch(ShowPermissionDialog)
    }

    //отслеживание можно ли запросить пермишн
    LaunchedEffect(coarsePermission.status.shouldShowRationale) {
        when (coarsePermission.status.shouldShowRationale) {
            true -> viewModel.dispatch(ShowPermissionDialog)
            false -> viewModel.dispatch(ShowGoToSettingsDialog)
        }
    }

    //отслеживание дали ли пермишн из настроек
    LifecycleResumeEffect(Unit) {
        val isGranted = coarsePermission.status.isGranted
        when {
            isGranted -> viewModel.dispatch(RequestedPermissionGranted)
            !isGranted && coarsePermission.status.shouldShowRationale -> viewModel.dispatch(
                ShowPermissionDialog
            )

            !isGranted && !coarsePermission.status.shouldShowRationale -> viewModel.dispatch(
                ShowGoToSettingsDialog
            )
        }

        onPauseOrDispose {}//do nothing
    }

    //отслеживание есть ли пермишн
    LaunchedEffect(coarseGranted) {
        if (coarseGranted) {
            viewModel.dispatch(RequestedPermissionGranted)
            return@LaunchedEffect
        }

        when {
            coarsePermission.status.shouldShowRationale -> {
                viewModel.dispatch(ShowPermissionDialog)
            }

            !coarsePermission.status.shouldShowRationale -> {
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
        if (state.isGoToSettingsDialogVisible) CoarseGoToSettingsDialog(viewModel::dispatch)
        if (state.isPermissionDialogVisible) CoarsePermissionDialog(viewModel::dispatch)
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            GoToSettings -> context.startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null)
                )
            )

            RequestPermission -> coarsePermission.launchPermissionRequest()
            OnPermissionGranted -> navigator.navigate(FineDestination())
        }
    }
}