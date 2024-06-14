package com.qwertyuiop.appentrypoint.ui.components.mainActivity.ui

import android.content.IntentFilter
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.qwertyuiop.appentrypoint.ui.components.mainActivity.navigation.NavGraphs
import com.qwertyuiop.appentrypoint.ui.components.mainActivity.utils.location.LocationBroadcastReceiver
import com.qwertyuiop.appentrypoint.ui.components.mainActivity.utils.location.LocationOnReceiveAction
import com.qwertyuiop.appentrypoint.ui.components.mainActivity.utils.network.ConnectivityObserver
import com.qwertyuiop.appentrypoint.ui.components.mainActivity.utils.network.ConnectivityObserver.Status.Available
import com.qwertyuiop.appentrypoint.ui.components.mainActivity.utils.network.ConnectivityObserver.Status.Unavailable
import com.qwertyuiop.appentrypoint.ui.theme.MainAppTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.test_task.entrypoint.R
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf


class MainActivity : AppCompatActivity() {

    private var locationBroadcastReceiver: LocationBroadcastReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(0),
            navigationBarStyle = SystemBarStyle.dark(0)
        )

        val locationFilter = IntentFilter("android.location.PROVIDERS_CHANGED")

        setContent {
            val locationManager = koinInject<LocationManager>()
            var locationEnabled by remember {
                mutableStateOf(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
            }
            locationBroadcastReceiver = koinInject<LocationBroadcastReceiver> {
                parametersOf(LocationOnReceiveAction { enabled -> locationEnabled = enabled })
            }

            val connectivityObserver = koinInject<ConnectivityObserver>()
            val status by connectivityObserver.observe().collectAsState(
                initial = Unavailable
            )

            applicationContext.registerReceiver(locationBroadcastReceiver, locationFilter)

            MainAppTheme {
                when {
                    status == Available && locationEnabled -> DestinationsNavHost(navGraph = NavGraphs.root)
                    !locationEnabled -> Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.geolocation_is_disabled),
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    status != Available -> Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.internet_connection_lost),
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}