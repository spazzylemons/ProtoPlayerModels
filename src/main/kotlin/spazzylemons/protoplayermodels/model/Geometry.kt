package spazzylemons.protoplayermodels.model

import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.util.math.Vector3f
import net.minecraft.client.util.math.Vector4f

class UVCoordinate(
    val u: Float,
    val v: Float,
)

class Vertex(
    val position: Vector3f,
    val uv: UVCoordinate,
)

class Face(
    val a: Vertex,
    val b: Vertex,
    val c: Vertex,
    val d: Vertex,
    val normal: Vector3f,
)

class QuadModel(
    val faces: Array<Face>,
) {
    fun render(
        matrices: MatrixStack,
        vertices: VertexConsumer,
        light: Int,
        overlay: Int,
        r: Float,
        g: Float,
        b: Float,
        a: Float,
    ) {
        matrices.push()
        try {
            val entry = matrices.peek()
            val entryModel = entry.model
            val entryNormal = entry.normal
            val pos = Vector4f()
            for (face in faces) {
                val n = face.normal.copy().apply { transform(entryNormal) }
                for (v in arrayOf(face.a, face.b, face.c, face.d)) {
                    pos.apply {
                        set(v.position.x, v.position.y, v.position.z, 1.0f)
                        transform(entryModel)
                    }
                    vertices.vertex(pos.x, pos.y, pos.z, r, g, b, a, v.uv.u, v.uv.v, overlay, light, n.x, n.y, n.z)
                }
            }
        } finally {
            matrices.pop()
        }
    }
}