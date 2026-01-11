plugins {
    id("dev.kikugie.stonecutter")
}
stonecutter active "1.21.11-neoforge"

tasks.register("runActiveVersion") {
    stonecutter.current.run {
        dependsOn(":${stonecutter.current?.project}:runClient")
    }
}
