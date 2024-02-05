buildscript {
    dependencies {
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.5")
        classpath("dev.icerock.moko:resources-generator:0.24.0-alpha-2")
    }
}

plugins {
    kotlin("jvm") apply false
    kotlin("multiplatform").apply(false)
    kotlin("android") apply false

    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    id("org.jetbrains.compose").apply(false)
    id("com.google.gms.google-services").version("4.3.15").apply(false)

}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
