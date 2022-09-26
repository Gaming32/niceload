package io.github.gaming32.niceload.client.mixin.vanilla;

import io.github.gaming32.niceload.api.NiceLoad;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SynchronousResourceReloader.class)
public class SynchronousResourceReloaderMixin {
    @Inject(
        method = "method_29490(Lnet/minecraft/util/profiler/Profiler;Lnet/minecraft/resource/ResourceManager;)V",
        at = @At("HEAD")
    )
    private void beginReload(Profiler profiler, ResourceManager resourceManager, CallbackInfo ci) {
        if (this instanceof IdentifiableResourceReloadListener) {
            NiceLoad.beginTask(((IdentifiableResourceReloadListener)this).getFabricId().toString());
        }
    }

    @Inject(
        method = "method_29490(Lnet/minecraft/util/profiler/Profiler;Lnet/minecraft/resource/ResourceManager;)V",
        at = @At("RETURN")
    )
    private void endReload(Profiler profiler, ResourceManager resourceManager, CallbackInfo ci) {
        if (this instanceof IdentifiableResourceReloadListener) {
            NiceLoad.endTask(((IdentifiableResourceReloadListener)this).getFabricId().toString());
        }
    }
}
