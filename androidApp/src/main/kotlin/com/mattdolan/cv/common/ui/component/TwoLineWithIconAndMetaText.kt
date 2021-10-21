package com.mattdolan.cv.common.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mattdolan.cv.androidApp.R

private val emptyListener: () -> Unit = {}

@Composable
fun TwoLineWithIconAndMetaText(
    supportingVisual: Painter,
    primaryText: String,
    secondaryText: String?,
    metadata: String,
    topLine: Boolean,
    bottomLine: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = emptyListener,
) {

    Box(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .clickable(onClick != emptyListener) { onClick() }
    ) {
        // Lines
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(72.dp)
                .fillMaxHeight()
        ) {
            if (topLine) {
                Box(
                    modifier = Modifier
                        .background(colorResource(R.color.list_item_connection))
                        .width(2.dp)
                        .weight(1f)
                )
            } else {
                Spacer(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                )
            }
            if (bottomLine) {
                Box(
                    modifier = Modifier
                        .background(colorResource(R.color.list_item_connection))
                        .width(2.dp)
                        .weight(1f)
                )
            } else {
                Spacer(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                )
            }
        }

        // Circle
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .width(72.dp)
                .fillMaxHeight()
        ) {
            Image(
                painter = supportingVisual,
                contentDescription = null,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .size(40.dp)
            )
        }

        // Text
        Column(
            modifier = Modifier.padding(
                top = 16.dp,
                bottom = 16.dp,
                start = 72.dp,
                end = 16.dp
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = primaryText,
                    fontSize = 16.sp,
                    fontFamily = roboto,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .alignByBaseline()
                        .weight(1f)
                )
                Text(
                    text = metadata,
                    fontSize = 14.sp,
                    fontFamily = roboto,
                    modifier = Modifier
                        .alignByBaseline()
                        .padding(start = 16.dp)
                )
            }
            if (!secondaryText.isNullOrBlank()) {
                Row(modifier = Modifier) {
                    Text(
                        text = secondaryText,
                        fontSize = 14.sp,
                        fontFamily = robotoSlab,
                        fontWeight = FontWeight.Light,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun PreviewTwoLineWithIconAndMetaText() {
    Column {
        TwoLineWithIconAndMetaText(
            supportingVisual = painterResource(R.drawable.ic_babylon_health),
            primaryText = "Senior Android Engineer a asfsdfsd sdf sdfsr dsfs",
            secondaryText = "Android platform team",
            metadata = "07/2018 - present",
            topLine = true,
            bottomLine = true,
            onClick = {},
            modifier = Modifier.background(Color.White)
        )

        Spacer(modifier = Modifier.height(32.dp))

        TwoLineWithIconAndMetaText(
            supportingVisual = painterResource(R.drawable.ic_company_placeholder),
            primaryText = "Senior Android Engineer a asfsdfsd sdf sdfsr dsfs",
            secondaryText = null,
            metadata = "07/2018 - present",
            topLine = true,
            bottomLine = false,
            onClick = {},
            modifier = Modifier.background(Color.White)
        )

        Spacer(modifier = Modifier.height(32.dp))

        TwoLineWithIconAndMetaText(
            supportingVisual = painterResource(R.drawable.ic_babylon_health),
            primaryText = "Senior Android Engineer",
            secondaryText = "Android platform team sdf sdrg rsrg rgfdrj rkgtnt",
            metadata = "07/2018 - present",
            topLine = false,
            bottomLine = true,
            onClick = {},
            modifier = Modifier.background(Color.White)
        )
    }
}
