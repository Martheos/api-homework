plugins {
    kotlin("jvm") version "1.9.21"
    id("io.ktor.plugin") version "2.3.11"
    kotlin("plugin.serialization") version "1.9.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}