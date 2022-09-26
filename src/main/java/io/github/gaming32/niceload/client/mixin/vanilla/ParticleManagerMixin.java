package io.github.gaming32.niceload.client.mixin.vanilla;

import io.github.gaming32.niceload.api.NiceLoad;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    @Unique
    private static final String niceload$TASK_NAME = "Particle Manager";

    @Inject(
        method = "reload(Lnet/minecraft/resource/ResourceReloader$Synchronizer;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;Lnet/minecraft/util/profiler/Profiler;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/concurrent/CompletableFuture;allOf([Ljava/util/concurrent/CompletableFuture;)Ljava/util/concurrent/CompletableFuture;"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void reload(
        ResourceReloader.Synchronizer synchronizer,
        ResourceManager manager,
        Profiler prepareProfiler,
        Profiler applyProfiler,
        Executor prepareExecutor,
        Executor applyExecutor,
        CallbackInfoReturnable<CompletableFuture<Void>> cir,
        Map<?, ?> map,
        CompletableFuture<?>[] completableFutures
    ) {
        NiceLoad.beginTask(niceload$TASK_NAME, completableFutures.length);
    }

    @Inject(
        method = "method_18831(Lnet/minecraft/util/profiler/Profiler;Ljava/util/Map;Lnet/minecraft/client/texture/SpriteAtlasTexture$Data;)V",
        at = @At("RETURN")
    )
    private void method_18831(Profiler profiler, Map<?, ?> map, SpriteAtlasTexture.Data data, CallbackInfo ci) {
        NiceLoad.endTask(niceload$TASK_NAME);
    }

    @Inject(
        method = "loadTextureList(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/Identifier;Ljava/util/Map;)V",
        at = @At("RETURN")
    )
    private void method_18833(ResourceManager resourceManager, Identifier id, Map<Identifier, List<Identifier>> result, CallbackInfo ci) {
        NiceLoad.addTaskProgress(niceload$TASK_NAME);
    }
}
