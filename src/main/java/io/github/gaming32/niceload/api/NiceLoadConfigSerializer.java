package io.github.gaming32.niceload.api;

import com.moandjiezana.toml.Toml;
import com.moandjiezana.toml.TomlWriter;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.ConfigSerializer;
import me.shedaniel.autoconfig.util.Utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SuppressWarnings("unused")
public class NiceLoadConfigSerializer implements ConfigSerializer<NiceLoadConfig> {
    private final Config definition;
    private final TomlWriter tomlWriter;

    @SuppressWarnings("WeakerAccess")
    public NiceLoadConfigSerializer(Config definition, Class<?> _c, TomlWriter tomlWriter) {
        this.definition = definition;
        this.tomlWriter = tomlWriter;
    }

    public NiceLoadConfigSerializer(Config definition, Class<?> _c) {
        this(definition, _c, new TomlWriter());
    }

    private Path getConfigPath() {
        return Utils.getConfigFolder().resolve(definition.name() + ".toml");
    }

    @Override
    public void serialize(NiceLoadConfig configIn) throws SerializationException {
        Path configPath = getConfigPath();
        NiceLoadTomlConfig config = NiceLoadTomlConfig.from(configIn);

        try {
            Files.createDirectories(configPath.getParent());

            tomlWriter.write(config, configPath.toFile());
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public NiceLoadConfig deserialize() throws SerializationException {
        Path configPath = getConfigPath();

        if (Files.exists(configPath)) {
            try {
                NiceLoadTomlConfig config = new Toml().read(configPath.toFile()).to(NiceLoadTomlConfig.class);

                return config.toConfig();
            } catch (IllegalStateException e) {
                throw new SerializationException(e);
            }
        } else {
            return createDefault();
        }
    }

    @Override
    public NiceLoadConfig createDefault() {
        return Utils.constructUnsafely(NiceLoadConfig.class);
    }
}
