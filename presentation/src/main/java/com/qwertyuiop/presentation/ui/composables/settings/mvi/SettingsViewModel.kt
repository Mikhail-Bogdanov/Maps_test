package com.qwertyuiop.presentation.ui.composables.settings.mvi

import androidx.appcompat.app.AppCompatDelegate.setApplicationLocales
import androidx.core.os.LocaleListCompat.forLanguageTags
import com.qwertyuiop.core.mviViewModel.MviViewModel
import com.qwertyuiop.presentation.ui.composables.settings.mvi.SettingsSideEffect.NavigateToFine
import com.qwertyuiop.presentation.ui.composables.settings.mvi.SettingsSideEffect.PopBackStack
import com.qwertyuiop.presentation.ui.composables.settings.utils.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect

class SettingsViewModel : MviViewModel<Any, SettingsSideEffect, SettingsEvent>(Any()) {
    override fun dispatch(event: SettingsEvent) {
        when (event) {
            SettingsEvent.BackButtonClicked -> backButtonClicked()
            SettingsEvent.FineLocationButtonClicked -> fineLocationButtonClicked()
            is SettingsEvent.LanguageClicked -> languageClicked(event.language)
        }
    }

    private fun backButtonClicked() = intent {
        postSideEffect(PopBackStack)
    }

    private fun fineLocationButtonClicked() = intent {
        postSideEffect(NavigateToFine)
    }

    private fun languageClicked(language: Language) = intent {
        withContext(Dispatchers.Main) {
            setApplicationLocales(forLanguageTags(language.tag))
        }
    }
}