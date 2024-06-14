package com.qwertyuiop.presentation.ui.composables.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapEvent
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapState
import org.osmdroid.views.MapView

@Composable
fun MapScreen(state: MapState, mapView: MapView, onEvent: (MapEvent) -> Unit) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            MapTopBar(onEvent)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            state.dialogsQueue.forEach { dialogType ->
                MapDialog(dialogType, onEvent)
            }
            AndroidView(
                factory = { mapView },
                modifier = Modifier.fillMaxSize(),
                update = { mapView ->
                    mapView.invalidate()
                }
            )
        }
    }
}