package com

import android.app.Application
import com.di.presentationModule
import com.data.di.dataModule
import com.domain.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TourismApp : Application() {
	override fun onCreate() {
		super.onCreate()
		startKoin {
			androidLogger(Level.DEBUG)
			androidContext(this@TourismApp)
			modules(
				listOf(
					presentationModule,
					domainModule,
					dataModule
				)
			)
		}
	}
}
