package io.github.thepoultryman.walllanterns.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.thepoultryman.walllanterns.WallLanterns;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;

import java.io.*;
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
                this.loadConfig();
            } else {
                InputStream inputStream = this.getClass().getResourceAsStream("/assets/" + WallLanterns.MOD_ID + "/" + WallLanterns.MOD_ID + ".json");
                if (inputStream != null) {
                    OutputStream outputStream = new FileOutputStream(configPath.toFile());
                    IOUtils.copy(inputStream, outputStream);
                    this.loadConfig();
                }
            }
        } catch (FileNotFoundException ignored) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadConfig() {
        Path configPath = Path.of(FabricLoader.getInstance().getConfigDir() + "/" + WallLanterns.MOD_ID + ".json");
        if (configPath.toFile().exists()) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Reader reader;
            try {
                reader = new FileReader(configPath.toFile());
                this.configuration = gson.fromJson(reader, ConfigType.class);
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (!this.configuration.isConfigUpToDate()) {
                WallLanterns.LOGGER.info("Config file is out of date, updating.");
                this.configuration.updateConfig();
                try {
                    Writer writer = new FileWriter(configPath.toFile());
                    gson.toJson(this.configuration, writer);
                    writer.close();
                } catch (IOException exception) {
                    WallLanterns.LOGGER.warn("Error updating configuration file. An attempt will be made next launch to update the file.", exception);
                }
            }
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
        private static final int EXPECTED_VANILLA_TYPE_LANTERNS = 2;

        public final List<String> vanillaTypeLanterns = new ArrayList<>();

        public boolean isConfigUpToDate() {
            return this.vanillaTypeLanterns.size() == EXPECTED_VANILLA_TYPE_LANTERNS;
        }

        public void updateConfig() {
            InputStream inputStream = this.getClass().getResourceAsStream("/assets/" + WallLanterns.MOD_ID + "/" + WallLanterns.MOD_ID + ".json");
            if (inputStream != null) {
                Gson gson = new Gson();
                Reader reader = new InputStreamReader(inputStream);
                ConfigType defaultConfig = gson.fromJson(reader, ConfigType.class);
                try { reader.close(); } catch (IOException ignored) {}

                for (String identifier : defaultConfig.vanillaTypeLanterns) {
                    if (!this.vanillaTypeLanterns.contains(identifier)) this.vanillaTypeLanterns.add(identifier);
                }
            }
        }
    }
}
