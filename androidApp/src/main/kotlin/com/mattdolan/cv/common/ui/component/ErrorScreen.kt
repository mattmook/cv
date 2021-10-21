package com.mattdolan.cv.experience

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mattdolan.cv.androidApp.R

@Composable
fun ErrorScreen(
    errorMessage: String,
    buttonTitle: String,
    buttonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier.fillMaxSize()) {
        val (image, text, button) = createRefs()

        Text(errorMessage, fontSize = 20.sp, modifier = Modifier.constrainAs(text) {
            top.linkTo(parent.top)
            bottom.linkTo(image.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })

        Image(
            painter = painterResource(R.drawable.ic_cv_failed),
            contentDescription = null,
            modifier = Modifier
                .width(128.dp)
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Button(onClick = buttonClick, modifier = Modifier.constrainAs(button) {
            top.linkTo(image.bottom)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {
            Text(buttonTitle)
        }
    }
}

@Preview
@Composable
fun PreviewError() {
    ErrorScreen(stringResource(R.string.sorry), stringResource(R.string.retry), {})
}
