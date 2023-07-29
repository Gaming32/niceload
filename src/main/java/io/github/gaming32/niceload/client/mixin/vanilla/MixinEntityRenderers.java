package io.github.gaming32.niceload.client.mixin.vanilla;

import com.google.common.collect.ImmutableMap;
import io.github.gaming32.niceload.api.NiceLoad;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderers;
import net.minecraft.entity.EntityType;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@SuppressWarnings("MissingUnique")
@Mixin(EntityRenderers.class)
public class MixinEntityRenderers {
    @Shadow @Final private static Map<EntityType<?>, EntityRendererFactory<?>> RENDERER_FACTORIES;
    @Shadow @Final private static Map<String, EntityRendererFactory<AbstractClientPlayerEntity>> PLAYER_RENDERER_FACTORIES;
    @Unique
    private static final String niceload$ENTITY_TASK_NAME = "Entity Renderers";
    private static final String niceload$PLAYER_TASK_NAME = "Player Renderers";

    @Inject(
        method = "reloadEntityRenderers(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;)Ljava/util/Map;",
        at = @At("HEAD")
    )
    private static void reloadEntityRenderersStart(
        EntityRendererFactory.Context ctx,
        CallbackInfoReturnable<Map<EntityType<?>, EntityRenderer<?>>> cir
    ) {
        NiceLoad.beginTask(niceload$ENTITY_TASK_NAME, RENDERER_FACTORIES.size());
    }

    @Inject(
        method = "reloadEntityRenderers(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;)Ljava/util/Map;",
        at = @At("RETURN")
    )
    private static void reloadEntityRenderersEnd(
        EntityRendererFactory.Context ctx,
        CallbackInfoReturnable<Map<EntityType<?>, EntityRenderer<?>>> cir
    ) {
        NiceLoad.endTask(niceload$ENTITY_TASK_NAME);
    }

    @Inject(
        method = "method_32174(Lcom/google/common/collect/ImmutableMap$Builder;Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;Lnet/minecraft/entity/EntityType;Lnet/minecraft/client/render/entity/EntityRendererFactory;)V",
        at = @At("HEAD")
    )
    private static void method_32174Start(
        ImmutableMap.Builder<?, ?> builder,
        EntityRendererFactory.Context context,
        EntityType<?> entityType,
        EntityRendererFactory<?> factory,
        CallbackInfo ci
    ) {
        NiceLoad.setTaskDescription(niceload$ENTITY_TASK_NAME, Registry.ENTITY_TYPE.getId(entityType));
    }

    @Inject(
        method = "method_32174(Lcom/google/common/collect/ImmutableMap$Builder;Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;Lnet/minecraft/entity/EntityType;Lnet/minecraft/client/render/entity/EntityRendererFactory;)V",
        at = @At("RETURN")
    )
    private static void method_32174End(
        ImmutableMap.Builder<?, ?> builder,
        EntityRendererFactory.Context context,
        EntityType<?> entityType,
        EntityRendererFactory<?> factory,
        CallbackInfo ci
    ) {
        NiceLoad.addTaskProgress(niceload$ENTITY_TASK_NAME);
    }

    @Inject(
        method = "reloadPlayerRenderers(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;)Ljava/util/Map;",
        at = @At("HEAD")
    )
    private static void reloadPlayerRenderersStart(
        EntityRendererFactory.Context ctx,
        CallbackInfoReturnable<Map<EntityType<?>, EntityRenderer<?>>> cir
    ) {
        NiceLoad.beginTask(niceload$PLAYER_TASK_NAME, PLAYER_RENDERER_FACTORIES.size());
    }

    @Inject(
        method = "reloadPlayerRenderers(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;)Ljava/util/Map;",
        at = @At("RETURN")
    )
    private static void reloadPlayerRenderersEnd(
        EntityRendererFactory.Context ctx,
        CallbackInfoReturnable<Map<EntityType<?>, EntityRenderer<?>>> cir
    ) {
        NiceLoad.endTask(niceload$PLAYER_TASK_NAME);
    }

    @Inject(
        method = "method_32175(Lcom/google/common/collect/ImmutableMap$Builder;Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;Ljava/lang/String;Lnet/minecraft/client/render/entity/EntityRendererFactory;)V",
        at = @At("HEAD")
    )
    private static void method_32175Start(
        ImmutableMap.Builder<?, ?> builder,
        EntityRendererFactory.Context context,
        String type,
        EntityRendererFactory<?> factory,
        CallbackInfo ci
    ) {
        NiceLoad.setTaskDescription(niceload$PLAYER_TASK_NAME, type);
    }

    @Inject(
        method = "method_32175(Lcom/google/common/collect/ImmutableMap$Builder;Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;Ljava/lang/String;Lnet/minecraft/client/render/entity/EntityRendererFactory;)V",
        at = @At("RETURN")
    )
    private static void method_32175End(
        ImmutableMap.Builder<?, ?> builder,
        EntityRendererFactory.Context context,
        String type,
        EntityRendererFactory<?> factory,
        CallbackInfo ci
    ) {
        NiceLoad.addTaskProgress(niceload$PLAYER_TASK_NAME);
    }
}
