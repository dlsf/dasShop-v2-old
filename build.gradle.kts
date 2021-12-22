plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.1"
}

group = "net.dasunterstrich"
version = "0.0.1"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://repo.codemc.org/repository/maven-public/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.18-R0.1-SNAPSHOT")

    implementation("com.google.inject:guice:5.0.1")
    implementation("dev.jorel.CommandAPI:commandapi-core:6.4.0") {
        exclude("com.destroystokyo.paper")
    }
}

tasks {
    shadowJar {
        fun relocate(origin: String) = relocate(origin, "net.dasunterstrich.dasshop.dependencies${origin.substring(origin.lastIndexOf('.'))}")

        relocate("com.google.common")
        relocate("de.tr7zw.nbtapi")
        relocate("de.tr7zw.nbtinjector")
        relocate("dev.jorel.commandapi")
        relocate("net.kyori.adventure")
        relocate("net.kyori.examination")
        relocate("org.checkerframework")

        minimize()
    }

    compileJava {
        options.encoding = "UTF-8"
    }

    processResources {
        filesMatching("**/plugin.yml") {
            expand(rootProject.project.properties)
        }

        // Always re-run this task
        outputs.upToDateWhen { false }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
        vendor.set(JvmVendorSpec.ADOPTOPENJDK)
    }
}