plugins {
    kotlin("jvm") version "1.9.22"
    java
    idea
}

group = "de.ventolotl"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
}
