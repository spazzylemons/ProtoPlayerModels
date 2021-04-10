package spazzylemons.protoplayermodels.mixin;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(ModelPart.class)
public interface CuboidsAccessorMixin {
    @Accessor
    ObjectList<ModelPart.Cuboid> getCuboids();
}
