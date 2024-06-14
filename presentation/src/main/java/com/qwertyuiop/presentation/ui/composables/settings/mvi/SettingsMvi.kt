package com.qwertyuiop.presentation.ui.composables.settings.mvi

import com.qwertyuiop.presentation.ui.composables.settings.utils.Language

sealed interface SettingsEvent {
    data object BackButtonClicked : SettingsEvent
    data object FineLocationButtonClicked : SettingsEvent
    data class LanguageClicked(
        val language: Language
    ) : SettingsEvent
}

sealed interface SettingsSideEffect {
    data object NavigateToFine : SettingsSideEffect
    data object PopBackStack : SettingsSideEffect
}