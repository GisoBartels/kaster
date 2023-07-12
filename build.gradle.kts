plugins {
    val kotlin = "1.8.20"
    val agp = "8.0.2"
    val compose = "1.4.1"

    kotlin("multiplatform") version kotlin apply false
    kotlin("plugin.serialization") version kotlin apply false
    kotlin("android") version kotlin apply false
    id("com.android.application") version agp apply false
    id("com.android.library") version agp apply false
    id("org.jetbrains.compose") version compose apply false
    id("com.google.devtools.ksp") version "1.8.20-1.0.11" apply false

    id("io.gitlab.arturbosch.detekt") version "1.23.0"
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    detekt {
        config = files("$rootDir/config/detekt.yml")
        buildUponDefaultConfig = true
    }

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.0")
        detektPlugins("com.twitter.compose.rules:detekt:0.0.26")
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}
