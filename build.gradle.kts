plugins {
    val kotlin = "2.1.21"
    val agp = "8.12.0"
    val compose = "1.8.2"

    kotlin("multiplatform") version kotlin apply false
    kotlin("plugin.compose") version kotlin apply false
    kotlin("plugin.serialization") version kotlin apply false
    kotlin("android") version kotlin apply false
    id("com.android.application") version agp apply false
    id("com.android.library") version agp apply false
    id("org.jetbrains.compose") version compose apply false
    id("com.google.devtools.ksp") version "2.1.21-2.0.2" apply false

    id("io.gitlab.arturbosch.detekt") version "1.23.8"
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    detekt {
        config.from("$rootDir/config/detekt.yml")
        buildUponDefaultConfig = true
    }

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
        detektPlugins("com.twitter.compose.rules:detekt:0.0.26")
    }
}
