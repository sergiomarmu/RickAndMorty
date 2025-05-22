plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.services)
}

android {
    namespace = "com.rickmorty.application"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rickmorty.application"
        minSdk = 32
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // region Dependencies
    implementation(project(":ui"))
    implementation(project(":di"))
    // endregion

    // region Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.compose)
    // endregion

    // region Firebase Boom
    implementation(platform(libs.firebase.boom))
    // endregion
}