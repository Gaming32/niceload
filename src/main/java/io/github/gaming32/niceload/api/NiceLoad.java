package io.github.gaming32.niceload.api;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.gui.screen.SplashOverlay;

public final class NiceLoad {
    private NiceLoad() {
        throw new AssertionError();
    }

    public static SplashScreen getSplashScreen() {
        final Overlay overlay = MinecraftClient.getInstance().getOverlay();
        if (!(overlay instanceof SplashOverlay)) {
            return null;
        }
        return (SplashScreen)overlay;
    }
}
