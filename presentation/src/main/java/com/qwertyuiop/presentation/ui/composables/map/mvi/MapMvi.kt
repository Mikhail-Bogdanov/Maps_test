package com.qwertyuiop.presentation.ui.composables.map.mvi

import com.qwertyuiop.localData.entities.PointEntity
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

data class MapState(
    val dialogsQueue: List<DialogType> = emptyList(),
    val currentRoadPath: List<GeoPoint>? = null,
    val currentRoadEndPoint: GeoPoint? = null
)

sealed interface MapEvent {
    data object SettingsButtonClicked : MapEvent
    data object Initialize : MapEvent
    data class OnMapClick(
        val dialogType: DialogType
    ) : MapEvent

    data object CloseFirstDialog : MapEvent
    data class NewMarkerAdded(
        val name: String,
        val point: GeoPoint
    ) : MapEvent

    data class RemoveMarkerClicked(
        val marker: Marker
    ) : MapEvent

    data class GetDirectionClicked(
        val startPoint: GeoPoint,
        val endPoint: GeoPoint
    ) : MapEvent

    data object CoarsePermissionIsNotGranted : MapEvent
    data object InvalidateMapMarkers : MapEvent
}

sealed interface MapSideEffect {
    data object NavigateToSettings : MapSideEffect

    data class UpdateMapPoints(
        val points: List<PointEntity>
    ) : MapSideEffect

    data class GetDirectionToMarker(
        val roadOverlay: Polyline?
    ) : MapSideEffect

    data object PopToCoarse : MapSideEffect
}