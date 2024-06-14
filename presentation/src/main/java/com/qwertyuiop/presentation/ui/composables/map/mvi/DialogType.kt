package com.qwertyuiop.presentation.ui.composables.map.mvi

import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker


sealed interface DialogType {
    data class OnMapClicked(
        val geoPoint: GeoPoint
    ) : DialogType

    data class OnMarkerClick(
        val marker: Marker
    ) : DialogType
}