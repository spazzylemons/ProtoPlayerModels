package spazzylemons.protoplayermodels.render

import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity

class ProtogenPlayerEntityModel<T : LivingEntity>(scale: Float) : PlayerEntityModel<T>(scale, false) {
    override fun render(
        matrices: MatrixStack?,
        vertices: VertexConsumer?,
        light: Int,
        overlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        // we'll do nothing, for now
    }
}