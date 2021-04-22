package spazzylemons.protoplayermodels.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import spazzylemons.protoplayermodels.ConstantsKt;

@Environment(EnvType.CLIENT)
@Mixin(AbstractClientPlayerEntity.class)
public abstract class PlayerMixin {
    @Shadow public abstract String getModel();

    @Inject(at = @At("HEAD"), method = "getSkinTexture", cancellable = true)
    public void getSkinTexture(CallbackInfoReturnable<Identifier> cir) {
        // TODO multiplayer support
        cir.setReturnValue(ConstantsKt.getTextureIdentifier());
    }
}
