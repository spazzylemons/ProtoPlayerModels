package spazzylemons.protoplayermodels.render

import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import spazzylemons.protoplayermodels.model.OBJLoader
import java.nio.charset.Charset

class ProtogenPlayerEntityModel<T : LivingEntity>(scale: Float) : PlayerEntityModel<T>(scale, false) {
    override fun render(
        matrices: MatrixStack,
        vertices: VertexConsumer,
        light: Int,
        overlay: Int,
        red: Float,
        green: Float,
        blue: Float,
        alpha: Float
    ) {
        matrices.push()
        try {
            proto.render(matrices, vertices, light, overlay, red, green, blue, alpha)
        } finally {
            matrices.pop()
        }
    }

    // temporary
    companion object {
        val proto = this::class.java.classLoader.getResource("proto.obj")!!.openStream().use {
            OBJLoader.load(it.reader(Charset.defaultCharset()))
        }
    }
}