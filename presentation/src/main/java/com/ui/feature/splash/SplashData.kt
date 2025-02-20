package com.ui.feature.splash

import android.content.res.Resources
import androidx.annotation.DrawableRes

data class SplashScreenData(
	@DrawableRes val image: Int,
	val title: String,
	val description: String
)
data class DetailsScreenData(
	@DrawableRes val image: Int,
	val title: String,
	val description: String
)