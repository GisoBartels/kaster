plugins {
    val kotlin = "1.8.10"
    val agp = "7.4.2"
    val compose = "1.3.1"

    kotlin("multiplatform") version kotlin apply false
    kotlin("plugin.serialization") version kotlin apply false
    kotlin("android") version kotlin apply false
    id("com.android.application") version agp apply false
    id("com.android.library") version agp apply false
    id("org.jetbrains.compose") version compose apply false
    id("com.google.devtools.ksp") version "$kotlin-1.0.9" apply false

    id("io.gitlab.arturbosch.detekt") version "1.22.0"
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    detekt {
        config = files("$rootDir/config/detekt.yml")
        buildUponDefaultConfig = true
    }

    dependencies {
        detektPlugins("com.twitter.compose.rules:detekt:0.0.26")
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}
