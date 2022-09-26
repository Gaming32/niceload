package io.github.gaming32.niceload.client.mixin.vanilla;

import com.mojang.datafixers.util.Pair;
import io.github.gaming32.niceload.api.NiceLoad;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.SpriteAtlasManager;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.Map;

@Mixin(ModelLoader.class)
public class MixinModelLoader {
    @Shadow @Final private Map<Identifier, Pair<SpriteAtlasTexture, SpriteAtlasTexture.Data>> spriteAtlasData;
    @Shadow @Final private Map<Identifier, UnbakedModel> modelsToBake;
    @Unique
    private static final String niceload$ATLAS_TASK_NAME = "Model Atlases";
    @Unique
    private static final String niceload$BAKE_TASK_NAME = "Model Baking";

    @Inject(
        method = "upload(Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/util/profiler/Profiler;)Lnet/minecraft/client/render/model/SpriteAtlasManager;",
        at = @At("HEAD")
    )
    private void beforeAtlas(
        TextureManager textureManager,
        Profiler profiler,
        CallbackInfoReturnable<SpriteAtlasManager> cir
    ) {
        NiceLoad.beginTask(niceload$ATLAS_TASK_NAME, spriteAtlasData.size());
    }

    @Inject(
        method = "upload(Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/util/profiler/Profiler;)Lnet/minecraft/client/render/model/SpriteAtlasManager;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/texture/SpriteAtlasTexture;upload(Lnet/minecraft/client/texture/SpriteAtlasTexture$Data;)V"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void beforeSingleAtlas(
        TextureManager textureManager,
        Profiler profiler,
        CallbackInfoReturnable<SpriteAtlasManager> cir,
        Iterator<?> var3,
        Pair<?, ?> pair,
        SpriteAtlasTexture spriteAtlasTexture,
        SpriteAtlasTexture.Data data
    ) {
        NiceLoad.setTaskDescription(niceload$ATLAS_TASK_NAME, spriteAtlasTexture.getId());
    }

    @Inject(
        method = "upload(Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/util/profiler/Profiler;)Lnet/minecraft/client/render/model/SpriteAtlasManager;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/texture/SpriteAtlasTexture;applyTextureFilter(Lnet/minecraft/client/texture/SpriteAtlasTexture$Data;)V",
            shift = At.Shift.AFTER
        )
    )
    private void afterSingleAtlas(
        TextureManager textureManager,
        Profiler profiler,
        CallbackInfoReturnable<SpriteAtlasManager> cir
    ) {
        NiceLoad.addTaskProgress(niceload$ATLAS_TASK_NAME);
    }

    @Inject(
        method = "upload(Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/util/profiler/Profiler;)Lnet/minecraft/client/render/model/SpriteAtlasManager;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V"
        )
    )
    private void afterAtlasBeforeBake(
        TextureManager textureManager,
        Profiler profiler,
        CallbackInfoReturnable<SpriteAtlasManager> cir
    ) {
        NiceLoad.endTask(niceload$ATLAS_TASK_NAME);
        NiceLoad.beginTask(niceload$BAKE_TASK_NAME, modelsToBake.size());
    }

    @Inject(
        method = "method_4733(Lnet/minecraft/util/Identifier;)V",
        at = @At("HEAD")
    )
    private void beforeSingleBake(Identifier id, CallbackInfo ci) {
        NiceLoad.setTaskDescription(niceload$BAKE_TASK_NAME, id);
    }

    @Inject(
        method = "method_4733(Lnet/minecraft/util/Identifier;)V",
        at = @At("RETURN")
    )
    private void afterSingleBake(Identifier id, CallbackInfo ci) {
        NiceLoad.addTaskProgress(niceload$BAKE_TASK_NAME);
    }

    @Inject(
        method = "upload(Lnet/minecraft/client/texture/TextureManager;Lnet/minecraft/util/profiler/Profiler;)Lnet/minecraft/client/render/model/SpriteAtlasManager;",
        at = @At("RETURN")
    )
    private void afterBake(
        TextureManager textureManager,
        Profiler profiler,
        CallbackInfoReturnable<SpriteAtlasManager> cir
    ) {
        NiceLoad.endTask(niceload$BAKE_TASK_NAME);
    }
}
