plugins {
    kotlin("jvm") version "1.9.0"
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
    `maven-publish`
}

group = "me.repeater64"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://libraries.minecraft.net")
    }
}


dependencies {


    // Source: https://mvnrepository.com/artifact/com.google.guava/guava
    implementation("com.google.guava:guava:21.0")

    // Source: https://mvnrepository.com/artifact/it.unimi.dsi/fastutil
    implementation("it.unimi.dsi:fastutil:8.2.1")
    
    implementation("com.mojang:datafixerupper:1.0.20")

    // Source: https://mvnrepository.com/artifact/com.sun.jna/platform
    implementation("net.java.dev.jna:jna-platform:5.13.0")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("ClientMainKt")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}