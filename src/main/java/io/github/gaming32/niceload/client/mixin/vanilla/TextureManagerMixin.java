package io.github.gaming32.niceload.client.mixin.vanilla;

import io.github.gaming32.niceload.api.LoadTask;
import io.github.gaming32.niceload.api.NiceLoad;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Executor;

@Mixin(TextureManager.class)
public class TextureManagerMixin {
    @Shadow @Final private Map<Identifier, AbstractTexture> textures;
    @Unique
    private static final String niceload$TASK_NAME = "Texture Manager";

    @Inject(
        method = "method_18167(Lnet/minecraft/resource/ResourceManager;Ljava/util/concurrent/Executor;Ljava/lang/Void;)V",
        at = @At("HEAD")
    )
    private void beforeReload(ResourceManager resourceManager, Executor executor, Void void_, CallbackInfo ci) {
        NiceLoad.beginTask(niceload$TASK_NAME, textures.size());
    }

    @Inject(
        method = "method_18167(Lnet/minecraft/resource/ResourceManager;Ljava/util/concurrent/Executor;Ljava/lang/Void;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/texture/AbstractTexture;registerTexture(Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/Identifier;Ljava/util/concurrent/Executor;)V"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void beforeRegisterTexture(
        ResourceManager resourceManager,
        Executor executor,
        Void void_,
        CallbackInfo ci,
        Iterator<?> iterator,
        Map.Entry<?, ?> entry,
        Identifier identifier,
        AbstractTexture abstractTexture
    ) {
        final LoadTask task = NiceLoad.getTask(niceload$TASK_NAME);
        if (task == null) return;
        task.setDescription(identifier.toString());
    }

    @Inject(
        method = "method_18167(Lnet/minecraft/resource/ResourceManager;Ljava/util/concurrent/Executor;Ljava/lang/Void;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/texture/AbstractTexture;registerTexture(Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/Identifier;Ljava/util/concurrent/Executor;)V",
            shift = At.Shift.AFTER
        )
    )
    private void afterRegisterTexture(ResourceManager resourceManager, Executor executor, Void void_, CallbackInfo ci) {
        final LoadTask task = NiceLoad.getTask(niceload$TASK_NAME);
        if (task == null) return;
        task.addProgress();
    }

    @Inject(
        method = "method_18167(Lnet/minecraft/resource/ResourceManager;Ljava/util/concurrent/Executor;Ljava/lang/Void;)V",
        at = @At("RETURN")
    )
    private void afterReload(ResourceManager resourceManager, Executor executor, Void void_, CallbackInfo ci) {
        NiceLoad.endTask(niceload$TASK_NAME);
    }
}
