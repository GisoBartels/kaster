plugins {
    val kotlin = "2.0.20"
    val agp = "8.5.2"
    val compose = "1.6.11"

    kotlin("multiplatform") version kotlin apply false
    kotlin("plugin.compose") version kotlin apply false
    kotlin("plugin.serialization") version kotlin apply false
    kotlin("android") version kotlin apply false
    id("com.android.application") version agp apply false
    id("com.android.library") version agp apply false
    id("org.jetbrains.compose") version compose apply false
    id("com.google.devtools.ksp") version "2.0.20-1.0.25" apply false

    id("io.gitlab.arturbosch.detekt") version "1.23.7"
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    detekt {
        config.from("$rootDir/config/detekt.yml")
        buildUponDefaultConfig = true
    }

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.6")
        detektPlugins("com.twitter.compose.rules:detekt:0.0.26")
    }
}
