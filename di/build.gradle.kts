plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.rickmorty"
    compileSdk = 34

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
    // region Dependencies
    implementation(project(":domain"))
    implementation(project(":data"))
    // endregion

    // region Kotlinx
    implementation(libs.kotlinx.serialization.json)
    // endregion

    // region SquareUp
    implementation(libs.retrofit)
    implementation(libs.retrofitKotlinConverter)
    implementation(libs.gson)
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    // endregion

    // region Paging
    implementation(libs.paging.runtime)
    // endregion

    // Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    //

    // region Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    // endregion
}