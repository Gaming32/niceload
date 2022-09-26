package io.github.gaming32.niceload.client.mixin.vanilla;

import io.github.gaming32.niceload.api.NiceLoad;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SynchronousResourceReloader.class)
public interface MixinSynchronousResourceReloader {
    @Inject(
        method = "method_29490(Lnet/minecraft/util/profiler/Profiler;Lnet/minecraft/resource/ResourceManager;)V",
        at = @At("HEAD")
    )
    default void beginReload(Profiler profiler, ResourceManager resourceManager, CallbackInfo ci) {
        final String name = NiceLoad.getReloaderName((ResourceReloader)this);
        if (!NiceLoad.isReloaderRegistered(name)) {
            NiceLoad.beginTask(name);
        }
    }

    @Inject(
        method = "method_29490(Lnet/minecraft/util/profiler/Profiler;Lnet/minecraft/resource/ResourceManager;)V",
        at = @At("RETURN")
    )
    default void endReload(Profiler profiler, ResourceManager resourceManager, CallbackInfo ci) {
        final String name = NiceLoad.getReloaderName((ResourceReloader)this);
        if (!NiceLoad.isReloaderRegistered(name)) {
            NiceLoad.endTask(name);
        }
    }
}
