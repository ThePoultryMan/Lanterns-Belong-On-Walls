## How?

For supported lanterns, place the lantern on the side of the block, and the lantern will attach to that side.

## Compatibility

For another mod's lanterns to be compatible, the other mod must add the compatibility (see here).

_Note: If you're using a resource pack that changes the lantern blockstate file, then the visual connection to the wall_ might _not appear._

<details>
<summary>For mod developers</summary>

Adding support for lanterns is a straightforward process.

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
       public void patchLanterns() {
           this.addLantern(new Identifier("example_mod", "example_lantern"));
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
4. Add the proper blockstates and models to your mod. Take a look [here](https://github.com/ThePoultryMan/Lanterns-Belong-On-Walls/tree/main/src/main/resources/assets) for a general idea of what it should look like.  
   The only major difference is that your blockstate file will need to include variants that have the facing property and variants that lack it. This will help to ensure compatibility when Lanterns Belong on Walls is not installed.

</details>