plugins {
    kotlin("jvm") version "2.1.0"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
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

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}
tasks {
    wrapper {
        gradleVersion = "8.11.1"
    }
}