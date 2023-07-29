package io.github.gaming32.niceload.client;

import com.mojang.logging.LogUtils;
import io.github.gaming32.niceload.api.NiceLoad;
import io.github.gaming32.niceload.api.NiceLoadConfig;
import io.github.gaming32.niceload.api.NiceLoadConfigSerializer;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
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

        AutoConfig.register(NiceLoadConfig.class, NiceLoadConfigSerializer::new);
        NiceLoadConfig config = AutoConfig.getConfigHolder(NiceLoadConfig.class).getConfig();

        SplashOverlay.MOJANG_RED = config.backgroundColor;
        NiceLoadConfig.INSTANCE = config;

        try {
            textRenderer = new SimpleTextRenderer();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        NiceLoad.registerReloader(BlockEntityRenderDispatcher.class.getSimpleName());
        NiceLoad.registerReloader(EntityRenderDispatcher.class.getSimpleName());
        NiceLoad.registerReloader(EntityModelLoader.class.getSimpleName());
    }

    public static NiceLoadMod getInstance() {
        return instance;
    }

    public SimpleTextRenderer getTextRenderer() {
        return textRenderer;
    }
}
