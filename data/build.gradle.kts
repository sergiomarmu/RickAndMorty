plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.ksp)
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

    packaging
        .resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
}

dependencies {
    // region Dependencies
    implementation(project(":domain"))
    // endregion

    // region Kotlinx
    implementation(libs.kotlinx.serialization.json)
    // endregion

    // region SquareUp
    implementation(libs.retrofit)
    // endregion

    // region Koin
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    // endregion

    // region Paging
    implementation(libs.paging.runtime)
    // endregion

    // region Room
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
    implementation(libs.room.paging)
    implementation(libs.room.ktx)
    // endregion

    // region Test
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.mockwebserver)
    testImplementation(libs.retrofitKotlinConverter)
    testImplementation(libs.kotlinx.coroutines.test)
    // endregion
}