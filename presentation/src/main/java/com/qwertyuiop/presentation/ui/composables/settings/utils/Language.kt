package com.qwertyuiop.presentation.ui.composables.settings.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.test_task.presentation.R

enum class Language(
    @StringRes private val titleId: Int,
    val tag: String?
) {
    English(
        R.string.english,
        "en"
    ),
    Russian(
        R.string.russian,
        "ru"
    ),
    Default(
        R.string.system_default,
        null
    );

    val title
        @Composable
        get() = stringResource(id = titleId)
}