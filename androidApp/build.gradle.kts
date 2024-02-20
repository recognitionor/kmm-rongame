plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "com.jhlee.kmm_rongame.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.jhlee.kmm_rongame.android"
        minSdk = 24
        targetSdk = 34
        versionCode = 143
        versionName = "1.4.3"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.activity:activity-compose:1.8.2")
}