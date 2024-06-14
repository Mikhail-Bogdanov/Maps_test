package com.qwertyuiop.presentation.di

import android.content.Context
import com.google.android.gms.location.LocationServices
import com.qwertyuiop.presentation.ui.composables.location.mvi.PermissionsViewModel
import com.qwertyuiop.presentation.ui.composables.map.mvi.MapViewModel
import com.qwertyuiop.presentation.ui.composables.settings.mvi.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.config.Configuration
import org.osmdroid.views.MapView

object PresentationModule {
    val module = module {
        viewModelOf(::PermissionsViewModel)
        viewModelOf(::MapViewModel)
        viewModelOf(::SettingsViewModel)
        factory { MapView(androidContext()) }
        single { androidContext().getSharedPreferences("map_prefs", Context.MODE_PRIVATE) }
        single { LocationServices.getFusedLocationProviderClient(androidContext()) }
        single {
            OSRMRoadManager(
                androidContext(),
                Configuration.getInstance().userAgentValue
            )
        }
    }
}