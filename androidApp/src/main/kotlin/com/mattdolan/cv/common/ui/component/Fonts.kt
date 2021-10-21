package com.mattdolan.cv.common.ui.component

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.mattdolan.cv.androidApp.R

val roboto = FontFamily(
    Font(R.font.roboto_regular),
    Font(R.font.roboto_bold, weight = FontWeight.Bold),
    Font(R.font.roboto_light, weight = FontWeight.Light),
    Font(R.font.roboto_medium, weight = FontWeight.Medium)
)

val robotoSlab = FontFamily(
    Font(R.font.roboto_slab_regular),
    Font(R.font.roboto_slab_light, weight = FontWeight.Light),
    Font(R.font.roboto_slab_light_italic, weight = FontWeight.Light, style = FontStyle.Italic)
)
