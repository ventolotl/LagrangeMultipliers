plugins {
    kotlin("jvm") version "1.9.22"
    id("application")
    id("org.openjfx.javafxplugin") version "0.0.8"
    java
    idea
}

group = "de.ventolotl"
version = "1.0.0"

repositories {
    mavenCentral()
}

javafx {
    version = "22.0.1"
    modules = listOf("javafx.controls")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    testImplementation(kotlin("test"))
}
