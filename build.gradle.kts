plugins {
    val kotlin = "1.7.20"
    val agp = "7.3.1"
    val compose = "1.2.2"

    kotlin("multiplatform") version kotlin apply false
    kotlin("plugin.serialization") version kotlin apply false
    kotlin("android") version kotlin apply false
    id("com.android.application") version agp apply false
    id("com.android.library") version agp apply false
    id("org.jetbrains.compose") version compose apply false
    id("com.google.devtools.ksp") version "$kotlin-1.0.8" apply false
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}
