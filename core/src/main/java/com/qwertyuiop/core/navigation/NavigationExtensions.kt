package com.qwertyuiop.core.navigation

import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.Direction

fun DestinationsNavigator.navigateClear(direction: Direction) {
    navigate(direction) {
        popUpTo("root") {
            inclusive = true
        }
    }
}