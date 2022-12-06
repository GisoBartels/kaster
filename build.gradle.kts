plugins {
    val kotlin = "1.7.20"
    val agp = "7.3.0"
    val compose = "1.2.1"

    kotlin("multiplatform") version kotlin apply false
    kotlin("android") version kotlin apply false
    id("com.android.application") version agp apply false
    id("com.android.library") version agp apply false
    id("org.jetbrains.compose") version compose apply false
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}
