package com.mattdolan.cv.experience

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.mattdolan.cv.androidApp.R

@Composable
@Suppress("EXPERIMENTAL_API_USAGE_ERROR")
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AndroidView(
            factory = { context ->
                AppCompatImageView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                }
            },
            modifier = Modifier
                .height(128.dp)
                .width(128.dp),
        ) {
            val drawable = ContextCompat.getDrawable(
                it.context,
                R.drawable.avd_cv_loading
            ) as AnimatedVectorDrawable
            it.setImageDrawable(drawable)
            drawable.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                override fun onAnimationEnd(drawable: Drawable) {
                    super.onAnimationEnd(drawable)
                    (drawable as AnimatedVectorDrawable).start()
                }
            })
            drawable.start()
        }
    }
}

@Preview
@Composable
fun PreviewLoading() {
    LoadingScreen()
}
