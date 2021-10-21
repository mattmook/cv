package com.mattdolan.cv.common.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.MainAxisAlignment
import com.mattdolan.cv.androidApp.R

@Composable
fun Chip(text: String, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .heightIn(min = 24.dp)
            //.clip(MaterialTheme.shapes.small)
            //.background(colorResource(R.color.chip_background))
            .border(
                1.dp,
                colorResource(R.color.chip_border),
                MaterialTheme.shapes.small
            )
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontFamily = roboto,
            color = colorResource(R.color.chip_text),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Preview
@Composable
fun PreviewChip() {
    FlowRow(
        modifier = Modifier.padding(16.dp).background(Color.White),
        mainAxisSpacing = 4.dp,
        crossAxisSpacing = 4.dp,
        mainAxisAlignment = MainAxisAlignment.Center
    ) {
        Chip("Android (10 yrs)")
        Chip("Kotlin (4 yrs)")
        Chip("Java (18 yrs)")
        Chip("Gradle")
        Chip("Dagger/Hilt")
        Chip("Koin")
        Chip("Coroutines")
        Chip("LiveData")
        Chip("RxJava")
        Chip("Retrofit")
    }
}
