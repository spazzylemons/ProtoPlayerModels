package spazzylemons.protoplayermodels.render

import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.entity.model.PlayerEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.util.math.Vector4f
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
        super.render(matrices, vertices, light, overlay, red, green, blue, alpha)
        // a test at rendering
        val entry = matrices.peek()
        val p = Vector4f(0F, 0F, 0F, 1F)
        p.transform(entry.model)
        try {
            matrices.push()
            matrices.translate(p.x.toDouble(), p.y.toDouble(), p.z.toDouble())
            suzanne.forEach {
                vertices.quad(entry, it, red, green, blue, light, overlay)
            }
        } finally {
            matrices.pop()
        }
    }

    // temporary
    companion object {
        val suzanne = this::class.java.classLoader.getResource("suzanne.obj")!!.openStream().use {
            OBJLoader.load(it.reader(Charset.defaultCharset()))
        }
    }
}