package io.github.gaming32.niceload.client.mixin.core;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.gaming32.niceload.api.LoadTask;
import io.github.gaming32.niceload.api.SplashScreen;
import io.github.gaming32.niceload.client.NiceLoadMod;
import it.unimi.dsi.fastutil.objects.Object2FloatArrayMap;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.SplashOverlay;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Mixin(SplashOverlay.class)
public class SplashOverlayMixin implements SplashScreen {
    @Shadow @Final private MinecraftClient client;
    @Unique
    private final Map<String, LoadTask> niceload$tasks = new LinkedHashMap<>();
    private final Object2FloatMap<LoadTask> niceload$taskProgress = new Object2FloatArrayMap<>();

    @Override
    public SplashOverlay getOverlay() {
        return (SplashOverlay)(Object)this;
    }

    @Override
    public LoadTask addTask(LoadTask task) {
        niceload$tasks.put(task.getName(), task);
        return task;
    }

    @Override
    public LoadTask getTask(String name) {
        return niceload$tasks.get(name);
    }

    @Inject(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/SplashOverlay;renderProgressBar(Lnet/minecraft/client/util/math/MatrixStack;IIIIF)V",
            shift = At.Shift.AFTER
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci, int i, int j, long l, float f, float g, float h, int k, int p, double d, int q, double e, int r, int s, float t) {
        final int textColor = MinecraftClient.getInstance().options.getMonochromeLogo().getValue()
            ? SplashOverlay.MOJANG_RED : SplashOverlay.MONOCHROME_BLACK;
        final float opacity = 1.0f - MathHelper.clamp(f, 0.0f, 1.0f);

        NiceLoadMod.getInstance().getTextRenderer().drawCenteredText(
            matrices,
            (Math.round(MathHelper.clamp(t, 0f, 100f) * 1000) / 10.0) + "%",
            i / 2, s - 3,
            (textColor & 0xffffff) | ((int)(opacity * 255) << 24)
        );

        final Iterator<LoadTask> it = niceload$tasks.values().iterator();
        int count = 0;
        int limit = (client.getWindow().getScaledHeight() - s - 20) / 20;
        int y = s + 20;
        while (it.hasNext()) {
            final LoadTask task = it.next();
            if (task.finished()) {
                it.remove();
                niceload$taskProgress.removeFloat(task);
                continue;
            }
            if (++count > limit) break;
            final float progress = task.getProgress01();
            final float visualProgress = MathHelper.clamp(
                niceload$taskProgress.getFloat(task) * 0.95f + progress * 0.05f, 0f, 1f
            );
            niceload$taskProgress.put(task, visualProgress);
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, h);
            renderProgressBar(matrices, i / 2 - r, y - 6, i / 2 + r, y + 7, opacity, visualProgress);
            String text = task.getName() + " - " +
                (Math.round(MathHelper.clamp(progress, 0f, 100f) * 1000) / 10.0) + "%";
            if (!task.getDescription().isEmpty()) {
                text += " - " + task.getDescription();
            }
            NiceLoadMod.getInstance().getTextRenderer().drawCenteredText(
                matrices,
                text,
                i / 2, y - 3,
                (textColor & 0xffffff) | ((int)(opacity * 255) << 24)
            );
            y += 20;
        }
    }

    private void renderProgressBar(MatrixStack matrices, int minX, int minY, int maxX, int maxY, float opacity, float progress) {
        int i = MathHelper.ceil((float)(maxX - minX - 2) * progress);
        int j = Math.round(opacity * 255.0f);
        int k = ColorHelper.Argb.getArgb(j, 255, 255, 255);
        SplashOverlay.fill(matrices, minX + 2, minY + 2, minX + i, maxY - 2, k);
        SplashOverlay.fill(matrices, minX + 1, minY, maxX - 1, minY + 1, k);
        SplashOverlay.fill(matrices, minX + 1, maxY, maxX - 1, maxY - 1, k);
        SplashOverlay.fill(matrices, minX, minY, minX + 1, maxY, k);
        SplashOverlay.fill(matrices, maxX, minY, maxX - 1, maxY, k);
    }

    @ModifyArg(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/SplashOverlay;renderProgressBar(Lnet/minecraft/client/util/math/MatrixStack;IIIIF)V"
        ),
        index = 2
    )
    private int progressBarMinY(int minY) {
        return minY - 1;
    }

    @ModifyArg(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/SplashOverlay;renderProgressBar(Lnet/minecraft/client/util/math/MatrixStack;IIIIF)V"
        ),
        index = 4
    )
    private int progressBarMaxY(int maxY) {
        return maxY + 2;
    }
}
