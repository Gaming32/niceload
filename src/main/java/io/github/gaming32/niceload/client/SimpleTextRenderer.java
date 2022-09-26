package io.github.gaming32.niceload.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public final class SimpleTextRenderer {
    private final int[] charWidth = new int[256];
    private final String allowedChars;

    private final AbstractTexture texture;

    @SuppressWarnings("deprecation")
    SimpleTextRenderer() throws IOException {
        final NativeImage image;
        try (final InputStream is = NiceLoadMod.class.getResourceAsStream("/default_font.png")) {
            //noinspection ConstantConditions
            image = NativeImage.read(is);
        }
        texture = new NativeImageBackedTexture(image);

        final int imageWidth = image.getWidth();
        final int[] pixels = image.makePixelArray();

        for (int k = 0; k < 256; k++) {
            final int i1 = k % 16;
            final int k1 = k / 16;
            int i2 = 7;
            while (i2 >= 0) {
                final int k2 = i1 * 8 + i2;
                boolean flag1 = true;
                int j3 = 0;
                while (j3 < 8 && flag1) {
                    final int l3 = (k1 * 8 + j3) * imageWidth;
                    final int j4 = pixels[k2 + l3] & 0xff;
                    if (j4 > 0) {
                        flag1 = false;
                    }
                    j3++;
                }
                if (!flag1) break;
                i2--;
            }
            if (k == 32) {
                i2 = 2;
            }
            charWidth[k] = i2 + 2;
        }

        //noinspection ConstantConditions
        try (final BufferedReader reader = new BufferedReader(
            new InputStreamReader(NiceLoadMod.class.getResourceAsStream("/font.txt"), StandardCharsets.UTF_8)
        )) {
            allowedChars = reader.lines().filter(l -> !l.startsWith("#")).collect(Collectors.joining());
        }
    }

    public void drawText(MatrixStack matrices, String s, int x, int y, int color) {
        if ((color & 0xff000000) == 0) {
            color |= 0xff000000;
        }
        RenderSystem.setShaderTexture(0, texture.getGlId());
        RenderSystem.setShaderColor(
            (color >> 16 & 0xff) / 255f,
            (color >> 8 & 0xff) / 255f,
            (color & 0xff) / 255f,
            (color >>> 24 & 0xff) / 255f
        );
        for (int i = 0; i < s.length(); i++) {
            final int index = allowedChars.indexOf(s.charAt(i));
            if (index != -1) {
                final int c = index + 32;
                final int cWidth = charWidth[c];
                DrawableHelper.drawTexture(
                    matrices,
                    x, y,
                    (c % 16) * 8, (c / 16) * 8,
                    cWidth, 8,
                    128, 128
                );
                x += cWidth;
            }
        }
    }

    public void drawCenteredText(MatrixStack matrices, String s, int x, int y, int color) {
        drawText(matrices, s, x - getStringWidth(s) / 2, y, color);
    }

    public int getStringWidth(String s) {
        int width = 0;
        for (int i = 0; i < s.length(); i++) {
            final int index = allowedChars.indexOf(s.charAt(i));
            if (index != -1) {
                width += charWidth[index + 32];
            }
        }
        return width;
    }
}
