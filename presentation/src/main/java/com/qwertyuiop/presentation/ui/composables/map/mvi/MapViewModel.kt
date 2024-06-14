package com.qwertyuiop.presentation.ui.composables.map.mvi

import com.qwertyuiop.core.mviViewModel.MviViewModel
import com.qwertyuiop.localData.entities.PointEntity
import com.qwertyuiop.localData.repository.PointsRepository
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapEvent.CloseFirstDialog
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapEvent.CoarsePermissionIsNotGranted
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapEvent.GetDirectionClicked
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapEvent.Initialize
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapEvent.NewMarkerAdded
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapEvent.OnMapClick
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapEvent.RemoveMarkerClicked
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapEvent.SettingsButtonClicked
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapSideEffect.GetDirectionToMarker
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapSideEffect.NavigateToSettings
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapSideEffect.PopToCoarse
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapSideEffect.UpdateMapPoints
import kotlinx.coroutines.flow.first
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker

class MapViewModel(
    private val pointsRepository: PointsRepository,
    private val roadManager: OSRMRoadManager
) : MviViewModel<MapState, MapSideEffect, MapEvent>(
    initialState = MapState()
) {
    init {
        dispatch(Initialize)
    }

    override fun dispatch(event: MapEvent) {
        when (event) {
            SettingsButtonClicked -> settingsButtonClicked()
            Initialize -> initialize()
            is OnMapClick -> onMapClicked(event.dialogType)
            CloseFirstDialog -> closeFirstDialog()
            is NewMarkerAdded -> newMarkerAdded(event.name, event.point)
            is GetDirectionClicked -> getDirectionClicked(event.startPoint, event.endPoint)
            is RemoveMarkerClicked -> removeMarkerClicked(event.marker)
            CoarsePermissionIsNotGranted -> popToCoarse()
            MapEvent.InvalidateMapMarkers -> invalidateMapMarkers()
        }
    }

    private fun invalidateMapMarkers() = intent {
        val points = pointsRepository.getAllPoints().first()
        postSideEffect(UpdateMapPoints(points))
        state.currentRoadPath?.let {
            val updatedRoad = ArrayList(it)

            val waypoints = roadManager.getRoad(updatedRoad)

            updatePath(waypoints, it.last())
        }
    }

    private fun popToCoarse() = intent {
        postSideEffect(PopToCoarse)
    }

    private fun getDirectionClicked(startPoint: GeoPoint, endPoint: GeoPoint) = intent {
        val waypoints = roadManager.getRoad(arrayListOf(startPoint, endPoint))
        updatePath(waypoints, endPoint)

        removeFirstDialog()
    }

    private fun removeMarkerClicked(marker: Marker) = intent {
        state.currentRoadEndPoint?.let {
            if (it.latitude == marker.position.latitude
                && it.longitude == marker.position.longitude
            ) {
                reduce { state.copy(currentRoadPath = null) }

                postSideEffect(GetDirectionToMarker(null))
            }
        }

        pointsRepository.removePointById(marker.id)
        removeFirstDialog()
    }

    private fun newMarkerAdded(name: String, point: GeoPoint) = intent {
        val pointEntity = PointEntity(
            latitude = point.latitude,
            longitude = point.longitude,
            name = name
        )
        pointsRepository.addPoint(pointEntity)
        removeFirstDialog()
    }

    private fun closeFirstDialog() = intent {
        removeFirstDialog()
    }

    private fun onMapClicked(dialogType: DialogType) = intent {
        val updatedDialogQueue = state.dialogsQueue.toMutableList()
        updatedDialogQueue.add(dialogType)

        reduce { state.copy(dialogsQueue = updatedDialogQueue) }
    }

    private fun initialize() = intent {
        pointsRepository.getAllPoints().collect { points ->
            postSideEffect(UpdateMapPoints(points))
        }
    }

    private fun settingsButtonClicked() = intent {
        postSideEffect(NavigateToSettings)
    }

    private suspend fun SimpleSyntax<MapState, MapSideEffect>.removeFirstDialog() {
        val updatedDialogQueue = state.dialogsQueue.toMutableList()
        updatedDialogQueue.removeFirst()

        reduce { state.copy(dialogsQueue = updatedDialogQueue) }
    }

    private suspend fun SimpleSyntax<MapState, MapSideEffect>.updatePath(
        waypoints: Road,
        endPoint: GeoPoint
    ) {
        val path = RoadManager.buildRoadOverlay(waypoints)

        reduce {
            state.copy(
                currentRoadPath = path.actualPoints.toList(),
                currentRoadEndPoint = endPoint
            )
        }

        postSideEffect(GetDirectionToMarker(path))
    }
}