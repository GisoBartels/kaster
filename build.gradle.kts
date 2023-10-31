plugins {
    val kotlin = "1.9.10"
    val agp = "8.1.2"
    val compose = "1.5.10"

    kotlin("multiplatform") version kotlin apply false
    kotlin("plugin.serialization") version kotlin apply false
    kotlin("android") version kotlin apply false
    id("com.android.application") version agp apply false
    id("com.android.library") version agp apply false
    id("org.jetbrains.compose") version compose apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false

    id("io.gitlab.arturbosch.detekt") version "1.23.1"
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    detekt {
        config = files("$rootDir/config/detekt.yml")
        buildUponDefaultConfig = true
    }

    dependencies {
        detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.1")
        detektPlugins("com.twitter.compose.rules:detekt:0.0.26")
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}
