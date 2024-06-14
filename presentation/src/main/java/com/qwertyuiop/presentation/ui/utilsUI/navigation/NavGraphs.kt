package com.qwertyuiop.presentation.ui.utilsUI.navigation

import com.qwertyuiop.presentation.ui.composables.destinations.CoarseDestination
import com.qwertyuiop.presentation.ui.composables.destinations.FineDestination
import com.qwertyuiop.presentation.ui.composables.destinations.MapDestination
import com.qwertyuiop.presentation.ui.composables.destinations.SettingsDestination
import com.qwertyuiop.presentation.ui.composables.destinations.StartDestination
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route


object NavGraphs {
    val PresentationNavGraph = object : NavGraphSpec {
        override val destinationsByRoute: Map<String, DestinationSpec<*>> = listOf(
            MapDestination,
            StartDestination,
            CoarseDestination,
            FineDestination,
            SettingsDestination
        ).associateBy { it.route }
        override val route: String = "presentation_root"
        override val startRoute: Route = StartDestination
    }
}