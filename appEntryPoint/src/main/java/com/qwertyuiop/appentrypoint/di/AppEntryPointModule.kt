package com.qwertyuiop.appentrypoint.di

import android.location.LocationManager
import com.qwertyuiop.appentrypoint.ui.components.mainActivity.utils.location.LocationBroadcastReceiver
import com.qwertyuiop.appentrypoint.ui.components.mainActivity.utils.network.ConnectivityObserver
import com.qwertyuiop.appentrypoint.ui.components.mainActivity.utils.network.NetworkConnectivityObserver
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

object AppEntryPointModule {
    val module = module {
        singleOf(::NetworkConnectivityObserver) bind ConnectivityObserver::class
        single { androidContext().getSystemService(LocationManager::class.java) }
        single { params ->
            LocationBroadcastReceiver(
                locationManager = get(),
                locationOnReceiveAction = params.get()
            )
        }
    }
}