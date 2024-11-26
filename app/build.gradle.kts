plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.android.marvel"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.android.marvel"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        buildConfigField("String", "BASE_URL", "\"https://gateway.marvel.com/\"")
        buildConfigField("String", "API_PRIVATE", "\"666403411b1415d0ae7c14a73e7160504ac83e29\"")
        buildConfigField("String", "API_PUBLIC", "\"15e69ffe02879d498e25896062349bc4\"")
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
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    // AndroidX Libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment)

    // Material Design
    implementation(libs.material)

    // Image Loading
    implementation(libs.coil)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Dependency Injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)

    androidTestImplementation(libs.mockito.android)

    testImplementation(libs.androidx.core.testing)

    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.hilt.android.testing)

    androidTestImplementation (libs.androidx.hilt.lifecycle.viewmodel)
    androidTestImplementation (libs.androidx.hilt.work)

    testImplementation (libs.androidx.lifecycle.livedata.ktx)
}
kapt {
    correctErrorTypes = true
}