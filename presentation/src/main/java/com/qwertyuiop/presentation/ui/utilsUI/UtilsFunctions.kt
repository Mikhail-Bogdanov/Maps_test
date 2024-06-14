package com.qwertyuiop.presentation.ui.utilsUI

import android.content.Context
import com.qwertyuiop.localData.entities.PointEntity
import com.test_task.presentation.R
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

object UtilsFunctions {
    fun PointEntity.toMapMarker(
        context: Context,
        mapView: MapView,
        markerId: String,
        onMarkerClick: (Marker) -> Unit
    ): Marker {
        val marker = Marker(mapView)
        marker.position = GeoPoint(latitude, longitude)
        marker.setTextIcon(context.getString(R.string.increased_name_box, name)) //osm не идеален
        marker.setOnMarkerClickListener { _, _ ->
            onMarkerClick(marker)
            true
        }
        marker.id = markerId
        return marker
    }
}