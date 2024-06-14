package com.qwertyuiop.appentrypoint.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.test_task.entrypoint.R

val Inter = FontFamily(
    Font(R.font.inter_400, W400),
    Font(R.font.inter_500, W500),
    Font(R.font.inter_600, W600),
)


val Typography = Typography(
    titleMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = W600,
        fontSize = 20.sp,
        lineHeight = 22.sp,
        textAlign = TextAlign.Center
    ),
    bodySmall = TextStyle(
        fontFamily = Inter,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        textAlign = TextAlign.Center
    ),
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = W500,
        fontSize = 18.sp,
        lineHeight = 20.sp,
        textAlign = TextAlign.Center
    ),
    displaySmall = TextStyle(
        fontFamily = Inter,
        fontWeight = W600,
        fontSize = 22.sp,
        lineHeight = 22.sp,
        textAlign = TextAlign.Center
    ),
    labelMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = W500,
        fontSize = 18.sp,
        lineHeight = 18.sp,
        textAlign = TextAlign.Center
    )
)