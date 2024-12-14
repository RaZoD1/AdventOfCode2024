plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    alias(libs.plugins.kotlin.jvm)
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}
val multikVersion="0.2.3"

dependencies {
    // Kotlin Matrix Math library
    implementation("org.jetbrains.kotlinx:multik-core:$multikVersion")
    implementation("org.jetbrains.kotlinx:multik-kotlin:$multikVersion")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}