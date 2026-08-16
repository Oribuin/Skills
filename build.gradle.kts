import java.io.ByteArrayOutputStream

plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version "9.4.1"
    id("de.eldoria.plugin-yml.bukkit") version "0.9.0"
}

group = "dev.oribuin"
version = "1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    disableAutoTargetJvm()
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

repositories {
    mavenCentral()
    mavenLocal()

    maven("https://repo.oribuin.dev/repository/maven-public/")
    maven("https://repo.rosewooddev.io/repository/public/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.codemc.org/repository/maven-public")
    maven("https://nexus.neetgames.com/repository/maven-snapshots/")
    maven("https://jitpack.io")
}

dependencies {
    // Commands, Configs & Database
    api("org.incendo:cloud-core:2.0.0")
    api("org.incendo:cloud-annotations:2.0.0")
    api("org.incendo:cloud-paper:2.0.0")
    api("org.spongepowered:configurate-yaml:4.2.0")
    api("com.zaxxer:HikariCP:4.0.3")
    api("dev.triumphteam:triumph-gui:3.1.13") {  // https://triumphteam.dev/docs/triumph-gui/
        exclude(group = "com.google.code.gson", module = "gson") // Remove GSON, Already included in spigot api
        exclude(group = "net.kyori", module = "*") // Remove kyori
    }

    // Additional Utilities
    api("net.objecthunter:exp4j:0.4.8")
    api("com.jeff-media:MorePersistentDataTypes:2.4.0")

    // Spigot
    compileOnly("io.papermc.paper:paper-api:26.2.build.+")
    compileOnly("org.jetbrains:annotations:23.0.0")

    // External Plugins
    compileOnly("me.clip:placeholderapi:2.12.3")
    compileOnly("com.arcaniax:HeadDatabase-API:1.3.2")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        exclude(group = "org.bukkit", module = "*")
    }
}

tasks {

    compileJava {
        this.options.compilerArgs.add("-parameters")
        this.options.isFork = true
        this.options.encoding = "UTF-8"
    }

    shadowJar {
        // add commit hash to the jar name
        this.archiveClassifier.set("")
        this.relocate("com.jeff_media.morepersistentdatatypes", "${project.group}.skills.libs.pdt")
        this.relocate("net.objecthunter.exp4j", "${project.group}.skills.libs.exp4j")
        this.relocate("dev.triumphteam.gui", "${project.group}.skills.libs.triumphgui")
        this.relocate("io.leangen.geantyref", "${project.group}.skills.libs.geantyref")
        this.relocate("org.incendo", "${project.group}.skills.libs.incendo")
        this.relocate("org.spongepowered", "${project.group}.skills.libs.spongepowered")
        this.relocate("com.zaxxer", "${project.group}.skills.libs.hikari")
        this.relocate("org.slf4j", "${project.group}.skills.libs.slf4j")
        this.minimize()
    }

    bukkit {
        this.main = "dev.oribuin.skills.SkillsPlugin"
        this.author = "Oribuin"
        this.description = "hello"
        this.version = "${project.version}"
        this.apiVersion = "26.1.2"
        this.foliaSupported = true
        this.softDepend = listOf("Vault", "HeadDatabase", "PlaceholderAPI")
    }
    
    build {
        this.dependsOn(shadowJar)
    }
}