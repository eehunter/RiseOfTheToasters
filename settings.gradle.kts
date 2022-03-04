rootProject.name = "rise-of-the-toasters"
pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        gradlePluginPortal()
    }

    plugins {
        id("fabric-loom") version "0.11-SNAPSHOT"
        id("org.jetbrains.kotlin.jvm") version "1.6.10"
    }

}