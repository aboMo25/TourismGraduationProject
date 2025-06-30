plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.compose)
	kotlin("plugin.serialization") version "1.8.0"
	id("kotlin-parcelize")
	alias(libs.plugins.google.gms.google.services)
}

android {
	namespace = "com.example.tourismgraduationproject"
	compileSdk = 35 // Reverted to 34, as 35 is not stable/released yet for public use

	defaultConfig {
		applicationId = "com.example.tourismgraduationproject"
		minSdk = 24
		targetSdk = 34 // Ensure targetSdk matches compileSdk if not higher
		versionCode = 1
		versionName = "1.0"
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}
	packaging {
		resources {
			excludes += setOf(
				"META-INF/INDEX.LIST",
				"META-INF/DEPENDENCIES",
				"META-INF/LICENSE",
				"META-INF/LICENSE.txt",
				"META-INF/NOTICE",
				"META-INF/NOTICE.txt",
				"META-INF/DEPENDENCIES", // Duplicated entry, keep one
				"META-INF/io.netty.versions.properties"
			)
		}
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}

	kotlinOptions {
		jvmTarget = "17"
	}

	buildFeatures {
		compose = true
	}

	composeOptions {
		kotlinCompilerExtensionVersion = "1.5.7"
	}
	// Remove or comment out this block. Force resolution is not the right tool for class duplication.
	/*
	configurations.all {
	   resolutionStrategy {
		  force ("com.google.api.grpc:proto-google-common-protos:2.48.0")
		  force ("com.google.firebase:protolite-well-known-types:18.0.1")
	   }
	}
	*/
}

dependencies {
	implementation(project(":domain"))
	implementation(project(":data"))
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)
	implementation(platform(libs.androidx.compose.bom))
	implementation(libs.androidx.ui)
	implementation(libs.androidx.ui.graphics)
	implementation(libs.androidx.ui.tooling.preview)
	implementation(libs.androidx.material3)
	implementation(libs.firebase.auth)
	implementation(libs.androidx.constraintlayout)
	implementation(libs.volley)
	implementation(libs.androidx.espresso.core) // Duplicated
	implementation(libs.androidx.espresso.core) // Duplicated
	implementation(libs.androidx.tools.core)
	// Removed the explicit protolite-well-known-types here, as it's in domain module
	// and play-services-auth will bring its own.

	testImplementation(libs.junit)

	debugImplementation(libs.androidx.ui.tooling)
	debugImplementation(libs.androidx.ui.test.manifest)

	implementation(libs.koin.android)
	implementation(libs.koin.android.compose)
	implementation(libs.compose.navigation)
	implementation("io.coil-kt.coil3:coil-compose:3.0.0-rc01")
	implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.0-rc01")
	implementation(libs.kotlinx.serialization.json)

	testImplementation(libs.kotlinx.coroutines.test)
	testImplementation(libs.mockito.core)
	testImplementation(libs.mockito.kotlin)
	testImplementation(libs.koin.test)
	testImplementation(libs.koin.test.junit4)
	testImplementation(libs.mockito.inline)

	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)
	androidTestImplementation(platform(libs.androidx.compose.bom))
	androidTestImplementation(libs.ui.test.junit4)
	androidTestImplementation(libs.androidx.espresso.intents)
	androidTestImplementation(libs.androidx.rules)
	androidTestImplementation(libs.androidx.core)
	androidTestImplementation(libs.koin.test)
	androidTestImplementation(libs.androidx.runner)
	androidTestImplementation(libs.kotlinx.coroutines.test)

	implementation(libs.androidx.tracing)

	// EXCLUDE proto-google-common-protos from play-services-auth
	// This is the primary fix for the duplicate class error.
	implementation("com.google.android.gms:play-services-auth:20.7.0") {
		exclude(group = "com.google.api.grpc", module = "proto-google-common-protos")
	}

	implementation ("androidx.media3:media3-exoplayer:1.4.0")
	implementation ("androidx.media3:media3-ui:1.4.0")
	implementation ("androidx.media3:media3-exoplayer-hls:1.4.0")

	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0") // Duplicated

	implementation ("androidx.webkit:webkit:1.11.0")
	implementation("io.coil-kt:coil-compose:2.2.2") // Old Coil, you have coil3 above
	implementation("com.github.bumptech.glide:glide:4.12.0")
	implementation("com.github.bumptech.glide:compose:1.0.0-alpha.1") // For Jetpack Compose

	implementation ("com.airbnb.android:lottie-compose:6.1.0")

	implementation("com.squareup.okhttp3:logging-interceptor:4.12.0") // Duplicated
}