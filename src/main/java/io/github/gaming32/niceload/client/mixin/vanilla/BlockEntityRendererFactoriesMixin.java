package io.github.gaming32.niceload.client.mixin.vanilla;

import com.google.common.collect.ImmutableMap;
import io.github.gaming32.niceload.api.NiceLoad;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
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

@Mixin(BlockEntityRendererFactories.class)
public class BlockEntityRendererFactoriesMixin {
    @Shadow @Final private static Map<BlockEntityType<?>, BlockEntityRendererFactory<?>> FACTORIES;
    @Unique
    private static final String niceload$TASK_NAME = "Block Entity Renderers";

    @Inject(
        method = "reload(Lnet/minecraft/client/render/block/entity/BlockEntityRendererFactory$Context;)Ljava/util/Map;",
        at = @At("HEAD")
    )
    private static void reloadStart(
        BlockEntityRendererFactory.Context args,
        CallbackInfoReturnable<Map<BlockEntityType<?>, BlockEntityRenderer<?>>> cir
    ) {
        NiceLoad.beginTask(niceload$TASK_NAME, FACTORIES.size());
    }

    @Inject(
        method = "reload(Lnet/minecraft/client/render/block/entity/BlockEntityRendererFactory$Context;)Ljava/util/Map;",
        at = @At("RETURN")
    )
    private static void reloadEnd(
        BlockEntityRendererFactory.Context args,
        CallbackInfoReturnable<Map<BlockEntityType<?>, BlockEntityRenderer<?>>> cir
    ) {
        NiceLoad.endTask(niceload$TASK_NAME);
    }

    @Inject(
        method = "method_32145(Lcom/google/common/collect/ImmutableMap$Builder;Lnet/minecraft/client/render/block/entity/BlockEntityRendererFactory$Context;Lnet/minecraft/block/entity/BlockEntityType;Lnet/minecraft/client/render/block/entity/BlockEntityRendererFactory;)V",
        at = @At("HEAD")
    )
    private static void method_32145Start(
        ImmutableMap.Builder<?, ?> builder,
        BlockEntityRendererFactory.Context context,
        BlockEntityType<?> type,
        BlockEntityRendererFactory<?> factory,
        CallbackInfo ci
    ) {
        NiceLoad.setTaskDescription(niceload$TASK_NAME, Registry.BLOCK_ENTITY_TYPE.getId(type));
    }

    @Inject(
        method = "method_32145(Lcom/google/common/collect/ImmutableMap$Builder;Lnet/minecraft/client/render/block/entity/BlockEntityRendererFactory$Context;Lnet/minecraft/block/entity/BlockEntityType;Lnet/minecraft/client/render/block/entity/BlockEntityRendererFactory;)V",
        at = @At("RETURN")
    )
    private static void method_32145End(
        ImmutableMap.Builder<?, ?> builder,
        BlockEntityRendererFactory.Context context,
        BlockEntityType<?> type,
        BlockEntityRendererFactory<?> factory,
        CallbackInfo ci
    ) {
        NiceLoad.addTaskProgress(niceload$TASK_NAME);
    }
}
