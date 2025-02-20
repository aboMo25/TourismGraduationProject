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
	compileSdk = 34  // Keep at 34 for now since 35 is not yet released

	defaultConfig {
		applicationId = "com.example.tourismgraduationproject"
		minSdk = 24
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

	implementation("com.google.android.gms:play-services-auth:20.7.0")
	implementation ("androidx.media3:media3-exoplayer:1.4.0")
	implementation ("androidx.media3:media3-ui:1.4.0")
	implementation ("androidx.media3:media3-exoplayer-hls:1.4.0")

	implementation ("androidx.webkit:webkit:1.11.0")




}
