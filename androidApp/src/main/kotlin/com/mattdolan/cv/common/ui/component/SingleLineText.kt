package com.mattdolan.cv.common.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp


@Composable
fun SingleLineText(text: String) {
    Text(text, fontFamily = roboto, fontSize = 16.sp, modifier = Modifier.fillMaxWidth())
}

@Preview
@Composable
fun PreviewSingleLineText() {
    SingleLineText("Redefined testing strategy and introduced screenshot and interaction testing. Additionally, standardised unit tests around JUnit 5 and automated the conversion of over 1000 unit tests from Spek 1 and 2.")
}
