plugins {
    kotlin("jvm") version "2.0.21"
}

group = "com.gngsn"
version = "1.0-SNAPSHOT"
var mockkVersion = "1.13.17"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.3")

    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:${mockkVersion}")
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}