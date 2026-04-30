import net.fabricmc.loom.task.RemapJarTask

plugins {
    id("dev.isxander.modstitch.base") version "0.8.5"
    id("me.modmuss50.mod-publish-plugin") version ("1.1.0")
}

fun prop(name: String, consumer: (prop: String) -> Unit) {
    (findProperty(name) as? String?)
        ?.let(consumer)
}

val minecraft = if (property("deps.minecraft")?.equals("latest") == true) {
    property("latest_minecraft") as String
} else {
    property("deps.minecraft") as String
}
modstitch {
    minecraftVersion = minecraft

    // If parchment doesnt exist for a version yet you can safely
    // omit the "deps.parchment" property from your versioned gradle.properties
    parchment {
        prop("deps.parchment") { mappingsVersion = it }
    }

    // This metadata is used to fill out the information inside
    // the metadata files found in the templates folder.
    metadata {
        modId = "walllanterns"
        prop("mod.name") { modName = it }
        modDescription = "Makes lanterns placeable on walls."
        prop("mod.version") { modVersion = it }
        modGroup = "io.github.thepoultryman"
        modAuthor = "ThePoultryMan"

        fun <K : Any, V : Any> MapProperty<K, V>.populate(block: MapProperty<K, V>.() -> Unit) {
            block()
        }

        replacementProperties.populate {
            // You can put any other replacement properties/metadata here that
            // modstitch doesn't initially support. Some examples below.
            put("mod_issue_tracker", "https://github.com/ThePoultryMan/Lanterns-Belong-On-Walls")
            prop("deps.forge_config_api_port_min") {
                put("forge_config_api_port_min_version", it)
            }
            prop("deps.min_fabric_api_version") {
                put("fabric_api_min_version", it)
            }
            put(
                "minecraft_version_range",
                // If a minimum version requirement exists
                if (hasProperty("deps.minecraft_min")) {
                    if (modstitch.isLoom) {
                        ">=${property("deps.minecraft_min")}" + if (property("deps.minecraft")?.equals("latest") == true) {
                            ""
                        } else {
                            "<= ${minecraftVersion.get()}"
                        }
                    } else {
                        "[${property("deps.minecraft_min")}, " + if (property("deps.minecraft")?.equals("latest") == true) {
                            ")"
                        } else {
                            "${minecraftVersion.get()}]"
                        }
                    }
                } else {
                    if (modstitch.isLoom) {
                        "${minecraftVersion.get()}"
                    } else {
                        "[${minecraftVersion.get()}]"
                    }
                }
            )
        }
    }

    // Fabric Loom (Fabric)
    loom {
        // It's not recommended to store the Fabric Loader version in properties.
        // Make sure its up to date.
        fabricLoaderVersion = "0.18.4"

        // Configure loom like normal in this block.
        configureLoom {}
    }

    // ModDevGradle (NeoForge, Forge, Forgelike)
    moddevgradle {
        prop("deps.neoforge") { neoForgeVersion = it }

        // Configures client and server runs for MDG, it is not done by default
        defaultRuns()
    }

    mixin {
        // You do not need to specify mixins in any mods.json/toml file if this is set to
        // true, it will automatically be generated.
        addMixinsToModManifest = true

        configs.register("walllanterns")
    }
}

// Stonecutter constants for mod loaders.
// See https://stonecutter.kikugie.dev/stonecutter/guide/comments#condition-constants
var constraint: String = name.split("-")[1]
stonecutter {
    constants.match(constraint, "fabric", "neoforge")

    replacements {
        string {
            direction = eval(current.version, ">=1.21.11")
            replace("ResourceLocation", "Identifier")
        }
        string {
            direction = eval(current.version, ">= 1.21.9")
            replace("getBlockRenderDispatcher", "blockRenderDispatcher")
        }
    }
}

repositories {
    maven {
        name = "Fuzs Mod Resources"
        url = uri("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
    }
}

// All dependencies should be specified through modstitch's proxy configuration.
// Wondering where the "repositories" block is? Go to "stonecutter.gradle.kts"
// If you want to create proxy configurations for more source sets, such as client source sets,
// use the modstitch.createProxyConfigurations(sourceSets["client"]) function.
dependencies {
    if (modstitch.isLoom) {
        modstitchModImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api_version")}")
        modstitchModApi("fuzs.forgeconfigapiport:forgeconfigapiport-fabric:${property("deps.forge_config_api_port")}")
    }
}

publishMods {
    if (modstitch.isLoom) {
        file.set(tasks.named<RemapJarTask>("remapJar").get().archiveFile)
    } else {
        file.set(tasks.jar.get().archiveFile)
    }

    var minMinecraftVersion = findProperty("deps.minecraft_min") as String?
    var versionRange = if (minMinecraftVersion != null) {
        "${minMinecraftVersion}-${minecraft}"
    } else {
        minecraft
    }
    var loader = if (modstitch.isLoom) {
        "fabric"
    } else {
        "neoforge"
    }
    displayName = "Lanterns Belong on Walls ${property("mod.version")}-${loader} for $versionRange"
    version = "${property("mod.version")}+${minecraft}-${loader}"
    type = STABLE
    if (modstitch.isLoom) {
        modLoaders.addAll("fabric", "quilt")
    } else {
        modLoaders.add("neoforge")
    }
    changelog = rootProject.file("CHANGELOG.md").readText()

    modrinth {
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        projectId = "V1CFwy2A"

        projectDescription.set(providers.fileContents(layout.projectDirectory.file("README.md")).asText)

        if (minMinecraftVersion != null) {
            minecraftVersionRange {
                start = minMinecraftVersion
                end = minecraft
            }
        } else {
            minecraftVersions.add(minecraft)
        }

        if (modstitch.isLoom) {
            requires("fabric-api")
        }
    }

    curseforge {
        accessToken = providers.environmentVariable("CURSEFORGE_API_KEY")
        projectId = "596474"

        if (minMinecraftVersion != null) {
            minecraftVersionRange {
                start = minMinecraftVersion
                end = minecraft
            }
        } else {
            minecraftVersions.add(minecraft)
        }

        if (modstitch.isLoom) {
            requires("fabric-api")
        }
    }
}