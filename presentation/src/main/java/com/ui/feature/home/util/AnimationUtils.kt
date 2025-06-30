package com.ui.feature.home.util
import androidx.compose.animation.*
import androidx.compose.animation.core.tween

object AnimationUtils {

	// Common durations
	private const val DEFAULT_DURATION = 300
	private const val FAST_DURATION = 150

	// Slide in from bottom with fade
	val FadeSlideInBottom: EnterTransition =
		fadeIn(tween(DEFAULT_DURATION)) + slideInVertically(tween(DEFAULT_DURATION)) { it / 2 }

	val FadeSlideOutBottom: ExitTransition =
		fadeOut(tween(DEFAULT_DURATION)) + slideOutVertically(tween(DEFAULT_DURATION)) { it / 2 }

	// Slide in from top with fade
	val FadeSlideInTop: EnterTransition =
		fadeIn(tween(DEFAULT_DURATION)) + slideInVertically(tween(DEFAULT_DURATION)) { -it }

	val FadeSlideOutTop: ExitTransition =
		fadeOut(tween(DEFAULT_DURATION)) + slideOutVertically(tween(DEFAULT_DURATION)) { -it }

	// Slide in from start horizontally with fade
	val FadeSlideInStart: EnterTransition =
		fadeIn(tween(DEFAULT_DURATION)) + slideInHorizontally(tween(DEFAULT_DURATION)) { -it }

	val FadeSlideOutStart: ExitTransition =
		fadeOut(tween(DEFAULT_DURATION)) + slideOutHorizontally(tween(DEFAULT_DURATION)) { -it }

	// Fade with scale
	val FadeScaleIn: EnterTransition =
		fadeIn(tween(FAST_DURATION)) + scaleIn(tween(FAST_DURATION))

	val FadeScaleOut: ExitTransition =
		fadeOut(tween(FAST_DURATION)) + scaleOut(tween(FAST_DURATION))

	// Expand and shrink with fade
	val FadeExpandIn: EnterTransition =
		fadeIn(tween(DEFAULT_DURATION)) + expandVertically(tween(DEFAULT_DURATION))

	val FadeShrinkOut: ExitTransition =
		fadeOut(tween(DEFAULT_DURATION)) + shrinkVertically(tween(DEFAULT_DURATION))

	// For lazy lists or staggered layout
	fun staggeredEnter(index: Int, delay: Int = 100): EnterTransition =
		fadeIn(tween(delayMillis = index * delay)) +
				slideInVertically(tween(delayMillis = index * delay)) { it / 3 }

	fun staggeredScaleEnter(index: Int, delay: Int = 100): EnterTransition =
		fadeIn(tween(delayMillis = index * delay)) +
				scaleIn(tween(delayMillis = index * delay))
}
