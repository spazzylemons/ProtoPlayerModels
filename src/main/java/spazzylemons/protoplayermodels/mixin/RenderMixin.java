package spazzylemons.protoplayermodels.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import spazzylemons.protoplayermodels.model.ClientData;
import spazzylemons.protoplayermodels.render.ProtogenPlayerEntityRenderer;

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderDispatcher.class)
public class RenderMixin {
    @Inject(at = @At("HEAD"), method = "getRenderer", cancellable = true)
    @SuppressWarnings("unchecked cast") // TODO see if this warning can be removed
    private <T extends Entity> void getRenderer(T entity, CallbackInfoReturnable<EntityRenderer<? super T>> cir) {
        // TODO do only on current player
        if (entity instanceof PlayerEntity) {
            if (ClientData.INSTANCE.getSettings().isEnabled()) {
                ProtogenPlayerEntityRenderer renderer = ClientData.INSTANCE.getRenderer();
                if (renderer != null) {
                    cir.setReturnValue((EntityRenderer<? super T>) renderer);
                }
            }
        }
    }
}
