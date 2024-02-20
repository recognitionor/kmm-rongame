@file:Suppress("OPT_IN_IS_NOT_ENABLED")

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")

    id("org.jetbrains.compose")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        ios.deploymentTarget = "16.0"

        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        podfile = project.file("../iosApp/Podfile")
        noPodspec()

//        pod("FirebaseCore") {
//            version = "10.7.0"
//        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class) implementation(
                    compose.components.resources
                )
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependencies {
            }
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
    }
}

android {
    namespace = "com.jhlee.kmm_rongame"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}


//multiplatformResources {
//    resourcesPackage.set("com.jhlee.kmm_rongame") // required
//    resourcesClassName.set("SharedRes") // optional, default MR
//}
//sqldelight {
//    databases {
//        create("AppDatabase") {
//            packageName.set("com.jhlee.kmm_rongame")
//
////            dialect("app.cash.sqldelight:mysql-dialect:2.0.0")
//            verifyMigrations.set(true)
//            deriveSchemaFromMigrations.set(true)
//            migrationOutputFileFormat.set(".sqm")
//            migrationOutputDirectory.set(file("sqldelight/migrations"))
//        }
//    }
//}
