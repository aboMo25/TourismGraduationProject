plugins {
	alias(libs.plugins.android.library)
	alias(libs.plugins.kotlin.android)
}

android {
	namespace = "com.example.domain"
	compileSdk = 35

	defaultConfig {
		minSdk = 24

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		consumerProguardFiles("consumer-rules.pro")
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
				"META-INF/DEPENDENCIES",
				"META-INF/LICENSE",
				"META-INF/LICENSE.txt",
				"META-INF/NOTICE",
				"META-INF/NOTICE.txt",
				"META-INF/INDEX.LIST",
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
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
	}
	kotlinOptions {
		jvmTarget = "11"
	}

}

dependencies {

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.appcompat)
	implementation(libs.material)
	implementation(libs.play.services.tasks)
	implementation(libs.play.services.base)
	implementation(libs.play.services.base)
	implementation(libs.androidx.credentials)
//	implementation(libs.protolite.well.known.types)
	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.androidx.espresso.core)

	api(libs.koin.core)
	implementation("io.insert-koin:koin-android:3.5.0")
	implementation("io.insert-koin:koin-androidx-compose:3.5.0")
//	implementation "io.insert-koin:koin-android:$koin_version"
	implementation ("io.insert-koin:koin-core:3.5.0")
	testImplementation ("io.insert-koin:koin-test:3.5.0")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
	implementation("com.squareup.retrofit2:retrofit:2.9.0")

}