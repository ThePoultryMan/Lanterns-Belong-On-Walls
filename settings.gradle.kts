pluginManagement {
    repositories {
        maven {
            name = "FabricMC Maven"
            url = uri("https://maven.fabricmc.net/")
        }
        maven {
            name = "Architectury Maven"
            url = uri("https://maven.architectury.dev/")
        }
        maven {
            name = "MinecraftForge Maven"
            url = uri("https://files.minecraftforge.net/maven/")
        }
        maven {
            name = "NeoForged Maven"
            url = uri("https://maven.neoforged.net/releases/")
        }

        gradlePluginPortal()
    }
}

plugins {
    id("dev.kikugie.stonecutter") version("0.7+")
}

stonecutter {
    kotlinController = true
    centralScript = "build.gradle.kts"

    create(rootProject) {
        fun mc(mcVersion: String, name: String = mcVersion) {
            version("$name-fabric", mcVersion)
            version("$name-neoforge", mcVersion)
        }

        // Configure your targets here!
        listOf("1.21.6").forEach {
            mc(it)
        }

        // This is the default target.
        // https://stonecutter.kikugie.dev/stonecutter/guide/setup#settings-settings-gradle-kts
        vcsVersion = "1.21.6-neoforge"
    }
}

rootProject.name = "ARRP But Different"
