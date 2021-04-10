package spazzylemons.protoplayermodels.render

import it.unimi.dsi.fastutil.objects.ObjectListIterator
import net.minecraft.client.model.Model
import net.minecraft.client.model.ModelPart
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.util.math.MatrixStack
import spazzylemons.protoplayermodels.model.QuadModel

class CustomModelPart(
    model: Model,
    private val quadModel: QuadModel,
) : ModelPart(model) {
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
        if (visible) {
            matrices.push()
            try {
                rotate(matrices)
                quadModel.render(matrices, vertices, light, overlay, red, green, blue, alpha)
            } finally {
                matrices.pop()
            }
        }
    }
}