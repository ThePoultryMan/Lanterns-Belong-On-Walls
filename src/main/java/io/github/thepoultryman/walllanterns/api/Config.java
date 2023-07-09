package io.github.thepoultryman.walllanterns.api;

import com.google.gson.Gson;
import io.github.thepoultryman.walllanterns.WallLanterns;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Config {
    public static final Config INSTANCE = new Config();

    public ConfigType configuration;

    public Config() {
        try {
            Path configPath = Path.of(FabricLoader.getInstance().getConfigDir() + "/" + WallLanterns.MOD_ID + ".json");
            if (configPath.toFile().exists()) {
                Gson gson = new Gson();
                Reader reader = new FileReader(configPath.toFile());

                this.configuration = gson.fromJson(reader, ConfigType.class);
                reader.close();
            }
        } catch (FileNotFoundException ignored) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Identifier> getVanillaTypeLanterns() {
        List<Identifier> identifiers = new ArrayList<>();
        for (String identifier : this.configuration.vanillaTypeLanterns) {
            identifiers.add(new Identifier(identifier));
        }

        return identifiers;
    }

    public static class ConfigType {
        public final List<String> vanillaTypeLanterns = new ArrayList<>();
    }
}
