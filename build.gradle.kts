import net.fabricmc.loom.task.RemapJarTask

plugins {
    id("dev.isxander.modstitch.base") version "0.7.0-unstable"
    id("me.modmuss50.mod-publish-plugin") version("0.8.4")
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

        fun <K, V> MapProperty<K, V>.populate(block: MapProperty<K, V>.() -> Unit) {
            block()
        }

        replacementProperties.populate {
            // You can put any other replacement properties/metadata here that
            // modstitch doesn't initially support. Some examples below.
            put("mod_issue_tracker", "https://github.com/ThePoultryMan/Lanterns-Belong-On-Walls")
            prop("deps.forge_config_api_port") {
                put("forge_config_api_port_version", it)
            }
            put("min_minecraft_version", property("deps.minecraft_min") as String)
            put("arrp_but_different_version", property("deps.arrp_but_different") as String)
            put("minecraft_upper_bound", if (property("deps.minecraft")?.equals("latest") == true) {
                ""
            } else {
                if (modstitch.isLoom) {
                    " <=$minecraftVersion"
                } else {
                    minecraftVersion.toString()
                }
            })
        }
    }

    // Fabric Loom (Fabric)
    loom {
        // It's not recommended to store the Fabric Loader version in properties.
        // Make sure its up to date.
        fabricLoaderVersion = "0.16.14"

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
    constants += arrayOf(
        "fabric" to constraint.equals("fabric"),
        "neoforge" to constraint.equals("neoforge"),
        "forge" to constraint.equals("forge"),
        "vanilla" to constraint.equals("vanilla")
    )
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
        modstitchModImplementation("net.fabricmc.fabric-api:fabric-api:${property("deps.fabric_api")}")
        modstitchModApi("fuzs.forgeconfigapiport:forgeconfigapiport-fabric:${property("deps.forge_config_api_port")}")
    }
    modstitchModImplementation("maven.modrinth:arrp-but-different:${property("deps.arrp_but_different")}+${minecraft}-${if (modstitch.isLoom) {
        "fabric"
    } else {
        "neoforge"
    }
    }")
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
    type = BETA
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

        requires("arrp-but-different")
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

        requires("arrp-but-different")
        if (modstitch.isLoom) {
            requires("fabric-api")
        }
    }
}