package com.qwertyuiop.presentation.ui.composables.settings

import android.Manifest.permission.ACCESS_FINE_LOCATION
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.qwertyuiop.presentation.ui.composables.destinations.FineDestination
import com.qwertyuiop.presentation.ui.composables.settings.mvi.SettingsEvent.FineLocationButtonClicked
import com.qwertyuiop.presentation.ui.composables.settings.mvi.SettingsEvent.LanguageClicked
import com.qwertyuiop.presentation.ui.composables.settings.mvi.SettingsSideEffect.NavigateToFine
import com.qwertyuiop.presentation.ui.composables.settings.mvi.SettingsSideEffect.PopBackStack
import com.qwertyuiop.presentation.ui.composables.settings.mvi.SettingsViewModel
import com.qwertyuiop.presentation.ui.composables.settings.utils.Language
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.test_task.presentation.R
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalPermissionsApi::class)
@Destination
@Composable
fun Settings(
    navigator: DestinationsNavigator
) {
    val finePermission = rememberPermissionState(ACCESS_FINE_LOCATION)

    val viewModel = koinViewModel<SettingsViewModel>()

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            SettingsTopBar(viewModel::dispatch)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Language.entries.forEach { language ->
                    Button(
                        onClick = {
                            viewModel.dispatch(LanguageClicked(language))
                        },
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = language.title,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
            if (!finePermission.status.isGranted) ListItem(
                headlineContent = {
                    Text(text = stringResource(R.string.allow_precise_location))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .clickable {
                        viewModel.dispatch(FineLocationButtonClicked)
                    },
                tonalElevation = 0.dp,
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    headlineColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            NavigateToFine -> navigator.navigate(FineDestination(fromSettings = true))
            PopBackStack -> navigator.popBackStack()
        }
    }
}