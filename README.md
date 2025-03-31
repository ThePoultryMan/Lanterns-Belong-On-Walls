## How?

For supported lanterns, place the lantern on the side of the block, and the lantern will attach to that side.

## Compatibility

For another mod's lanterns to be compatible, the other mod must add the compatibility.

_Note: If you're using a resource pack that changes the lantern blockstate file, then the visual connection to the wall_ might _not appear._

<details>
<summary>For mod developers</summary>

The basics for adding support for lanterns differs between mod loaders.

_The details below are for Fabric/Architectury Loom._

### For NeoForge

<details>

1. Add the Gradle dependency:
   ```gradle
   repositories {
       //...
       maven {
           name = "Modrinth"
           url = "https://api.modrinth.com/maven"
       }
   }

   dependencies {
       //...
       modImplementation "maven.modrinth:lanterns-bow:${lanterns_bow_version}"
   }
   ```
   Refer to the versions page for the most recent version.  
   _Read about the Modrinth Maven [here](https://support.modrinth.com/en/articles/8801191-modrinth-maven)._
2. In your mod constructor, listen for the mod bus event, then add the lanterns:
   ```java
   public ExampleModContstructor(IEventBus modBus) {
       modBus.addListener((WallLanternsEvent event) -> {
           event.addLantern(new WallLantern(
                   WallLantern.Type.StandardCutout,
                   ResourceLocation.parse("example:example_lantern"))
           );
           event.addLantern(new WallLantern(
                   WallLantern.Type.Standard,
                   ResourceLocation.parse("example:example_lantern_two"))
           );
       });
   }
   ```

</details>

### For Fabric

<details>

1. Add the Gradle dependency:
    ```gradle
    repositories {
        //...
        maven {
            name = "Modrinth"
            url = "https://api.modrinth.com/maven"
        }
    }

    dependencies {
        //...
        modImplementation "maven.modrinth:lanterns-bow:${lanterns_bow_version}"
    }
    ```
   Refer to the versions page for the most recent version.  
   _Read about the Modrinth Maven [here](https://support.modrinth.com/en/articles/8801191-modrinth-maven)._
2. Create an entrypoint class:
   ```java
   public class ExampleLanternModWall implements WallLanternsEntrypoint {
       @Override
       public void registerLanterns(WallLanternsRegistry registry) {
           registry.registerLantern(ResourceLocation.fromNamespaceAndPath("examplemod", "lantern"));
       }
   }
   ```
3. Add the entrypoint to your `fabric.mod.json`:
   ```json
   ...
   "entrypoints": {
       ...
       "walllanterns": "com.example.mod.examplemod.ExampleLanternModWall"
   }
   ```

</details>

### Lantern Types

_Unless otherwise specified all models for lanterns are derived from the standing
model of the provided lantern._

There are a few lantern types available:
* Standard - Uses the vanilla standing lantern shape.
* StandardCutout - Designed for NeoForge. Follows the same rules as Standard,
  but forces the use of the cutout Render Type on NeoForge.

</details>