package com.qwertyuiop.presentation.ui.composables.map

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapEvent
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapEvent.SettingsButtonClicked
import com.test_task.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapTopBar(onEvent: (MapEvent) -> Unit) = TopAppBar(
    title = {
        Text(
            text = stringResource(R.string.maps_test_task),
            style = MaterialTheme.typography.titleMedium
        )
    },
    actions = {
        IconButton(
            onClick = { onEvent(SettingsButtonClicked) }
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null
            )
        }
    },
    colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onPrimary,
        actionIconContentColor = MaterialTheme.colorScheme.onPrimary
    )
)