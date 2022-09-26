package io.github.gaming32.niceload.client;

import com.mojang.logging.LogUtils;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.UncheckedIOException;

public class NiceLoadMod implements PreLaunchEntrypoint {
    public static final Logger LOGGER = LogUtils.getLogger();
    private static NiceLoadMod instance;

    private SimpleTextRenderer textRenderer;

    public NiceLoadMod() {
        instance = this;
    }

    @Override
    public void onPreLaunch() {
        LOGGER.info("Early bird gets the worm!");
        try {
            textRenderer = new SimpleTextRenderer();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static NiceLoadMod getInstance() {
        return instance;
    }

    public SimpleTextRenderer getTextRenderer() {
        return textRenderer;
    }
}
