plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.rickmorty.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 32

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    // region kotlinx
    implementation(libs.kotlinx.coroutines.core)
    // endregion kotlinx

    // region Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.annotations)
    implementation(libs.koin.ksp)
    // endregion
}