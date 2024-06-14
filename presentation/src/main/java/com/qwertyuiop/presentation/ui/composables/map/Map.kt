package com.qwertyuiop.presentation.ui.composables.map

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.FusedLocationProviderClient
import com.qwertyuiop.core.navigation.navigateClear
import com.qwertyuiop.presentation.ui.composables.destinations.CoarseDestination
import com.qwertyuiop.presentation.ui.composables.destinations.SettingsDestination
import com.qwertyuiop.presentation.ui.composables.map.mvi.DialogType
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapEvent
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapEvent.CoarsePermissionIsNotGranted
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapEvent.InvalidateMapMarkers
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapEvent.OnMapClick
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapSideEffect.GetDirectionToMarker
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapSideEffect.NavigateToSettings
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapSideEffect.PopToCoarse
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapSideEffect.UpdateMapPoints
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapViewModel
import com.qwertyuiop.presentation.ui.utilsUI.UtilsFunctions.toMapMarker
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@OptIn(ExperimentalPermissionsApi::class)
@Destination
@Composable
fun Map(
    navigator: DestinationsNavigator
) {
    val coarsePermission = rememberPermissionState(ACCESS_COARSE_LOCATION)

    val mapView = koinInject<MapView>()
    val fusedLocationClient = koinInject<FusedLocationProviderClient>()
    val context = LocalContext.current
    val sharedPreferences = koinInject<SharedPreferences>() //for osm map

    val viewModel = koinViewModel<MapViewModel>()
    val state = viewModel.collectAsState().value

    if (!coarsePermission.status.isGranted) {
        //если юзер отозвал полностью разрешение на геолокацию на экране Fine
        viewModel.dispatch(CoarsePermissionIsNotGranted)
    }

    LifecycleResumeEffect(Unit) {
        mapView.onResume()

        setupMapView(
            context,
            fusedLocationClient,
            mapView,
            sharedPreferences,
            viewModel::dispatch
        )

        viewModel.dispatch(InvalidateMapMarkers)

        onPauseOrDispose {
            mapView.onPause()
        }
    }

    MapScreen(state, mapView, viewModel::dispatch)

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            NavigateToSettings -> navigator.navigate(SettingsDestination)
            is UpdateMapPoints -> {
                mapView.overlays.removeAll { it is Marker }
                sideEffect.points.forEach { point ->
                    val marker = point.toMapMarker(context, mapView, point.id) {
                        viewModel.dispatch(OnMapClick(DialogType.OnMarkerClick(it)))
                    }

                    mapView.overlays.add(marker)
                }

                mapView.invalidate()
            }

            is GetDirectionToMarker -> {
                mapView.overlays.removeAll { it is Polyline }
                if (sideEffect.roadOverlay != null)
                    mapView.overlays.add(sideEffect.roadOverlay)

                mapView.invalidate()
            }

            PopToCoarse -> navigator.navigateClear(CoarseDestination)
        }
    }
}

@SuppressLint("MissingPermission") //no it doesn't
private fun setupMapView(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    mapView: MapView,
    sharedPreferences: SharedPreferences,
    onEvent: (MapEvent) -> Unit
) {
    Configuration.getInstance().load(context, sharedPreferences)

    mapView.setTileSource(TileSourceFactory.MAPNIK)
    mapView.minZoomLevel = 5.0
    mapView.maxZoomLevel = 20.0
    mapView.setMultiTouchControls(true)

    mapView.controller.zoomTo(10.0)
    mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)

    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        val myLocation = GeoPoint(location.latitude, location.longitude)
        mapView.controller.animateTo(myLocation)
        mapView.controller.setCenter(myLocation)
    }

    val mapEventsReceiver = object : MapEventsReceiver {
        override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
            //do nothing
            return false
        }

        override fun longPressHelper(p: GeoPoint?): Boolean {
            p?.let {
                onEvent(OnMapClick(DialogType.OnMapClicked(p)))
                true
            }

            return false
        }
    }
    val mapEventsOverlay = MapEventsOverlay(mapEventsReceiver)

    val myLocationOverlay = MyLocationNewOverlay(mapView)
    myLocationOverlay.enableMyLocation()

    mapView.overlays.add(myLocationOverlay)
    mapView.overlays.add(mapEventsOverlay)

    mapView.invalidate()
}