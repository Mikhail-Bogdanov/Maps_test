package com.qwertyuiop.presentation.ui.composables.start

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.qwertyuiop.presentation.ui.composables.destinations.CoarseDestination
import com.qwertyuiop.presentation.ui.composables.destinations.FineDestination
import com.qwertyuiop.presentation.ui.composables.destinations.MapDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.test_task.presentation.R

@OptIn(ExperimentalPermissionsApi::class)
@Destination
@Composable
fun Start(
    navigator: DestinationsNavigator
) {
    val coarsePermission = rememberPermissionState(ACCESS_COARSE_LOCATION)
    val finePermission = rememberPermissionState(ACCESS_FINE_LOCATION)

    val coarseGranted = coarsePermission.status.isGranted
    val fineGranted = finePermission.status.isGranted

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.welcome_to_maps_test_task),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .fillMaxWidth()
        )
        Button(
            onClick = {
                when {
                    coarseGranted && fineGranted -> navigator.navigate(MapDestination)
                    !coarseGranted -> navigator.navigate(CoarseDestination)
                    !fineGranted -> navigator.navigate(FineDestination())
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = stringResource(R.string.start),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}