package com.qwertyuiop.presentation.ui.composables.settings

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.qwertyuiop.presentation.ui.composables.settings.mvi.SettingsEvent
import com.qwertyuiop.presentation.ui.composables.settings.mvi.SettingsEvent.BackButtonClicked
import com.test_task.presentation.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(onEvent: (SettingsEvent) -> Unit) = CenterAlignedTopAppBar(
    title = {
        Text(
            text = stringResource(R.string.settings),
            style = MaterialTheme.typography.titleMedium
        )
    },
    navigationIcon = {
        IconButton(
            onClick = { onEvent(BackButtonClicked) }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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