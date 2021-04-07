package spazzylemons.protoplayermodels.model

import net.minecraft.client.render.model.BakedQuad
import net.minecraft.client.util.math.Vector3f
import net.minecraft.util.math.Direction
import java.io.Reader

object OBJLoader {
    // yes, it's regex. no i don't care, this won't run often enough for me to feel like optimizing it.
    private const val NUMBER = "(-?\\d+(?:\\.\\d+)?)"
    private const val FACE = "(\\d+)/(\\d+)/\\d+"

    private val VERTEX = Regex("v $NUMBER $NUMBER $NUMBER")
    private val UV = Regex("vt $NUMBER $NUMBER")
    private val NORMAL = Regex("vn $NUMBER $NUMBER $NUMBER")
    private val QUAD = Regex("f $FACE $FACE $FACE $FACE")
    private val TRI = Regex("f $FACE $FACE $FACE")

    fun load(resource: Reader): List<BakedQuad> {
        val vertices = mutableListOf<Vector3f>()
        val uvs = mutableListOf<UVCoordinate>()
        val normals = mutableListOf<Vector3f>()
        val quads = mutableListOf<BakedQuad>()

        resource.forEachLine { line ->
            VERTEX.find(line)?.let { vertex ->
                vertices += Vector3f(
                    vertex.groups[1]!!.value.toFloat(),
                    -vertex.groups[2]!!.value.toFloat(), // flip y
                    vertex.groups[3]!!.value.toFloat(),
                )
                return@forEachLine
            }
            UV.find(line)?.let { vertex ->
                uvs += UVCoordinate(
                    vertex.groups[1]!!.value.toFloat(),
                    vertex.groups[2]!!.value.toFloat(),
                )
                return@forEachLine
            }
            NORMAL.find(line)?.let { vertex ->
                normals += Vector3f(
                    vertex.groups[1]!!.value.toFloat(),
                    vertex.groups[2]!!.value.toFloat(),
                    vertex.groups[3]!!.value.toFloat(),
                )
                return@forEachLine
            }
            QUAD.find(line)?.let { vertex ->
                val (face1, face2, face3, face4) = (0..3).map { 1 + it * 2 }.map {
                    vertices[vertex.groups[it]!!.value.toInt() - 1] to uvs[vertex.groups[it + 1]!!.value.toInt() - 1]
                }
                val (v1, uv1) = face1
                val (v2, uv2) = face2
                val (v3, uv3) = face3
                val (v4, uv4) = face4
                quads += BakedQuad(
                    intArrayOf(
                        // face 1
                        v1.x.toBits(),
                        v1.y.toBits(),
                        v1.z.toBits(),
                        0,
                        uv1.u.toBits(),
                        uv1.v.toBits(),
                        0,
                        0,
                        // face 2
                        v2.x.toBits(),
                        v2.y.toBits(),
                        v2.z.toBits(),
                        0,
                        uv2.u.toBits(),
                        uv2.v.toBits(),
                        0,
                        0,
                        // face 3
                        v3.x.toBits(),
                        v3.y.toBits(),
                        v3.z.toBits(),
                        0,
                        uv3.u.toBits(),
                        uv3.v.toBits(),
                        0,
                        0,
                        // face 4
                        v4.x.toBits(),
                        v4.y.toBits(),
                        v4.z.toBits(),
                        0,
                        uv4.u.toBits(),
                        uv4.v.toBits(),
                        0,
                        0,
                    ),
                    -1,
                    Direction.UP, // todo???
                    null,         // "
                    true,
                )
                return@forEachLine
            }
            TRI.find(line)?.let { vertex ->
                val (face1, face2, face3) = (0..2).map { 1 + it * 2 }.map {
                    vertices[vertex.groups[it]!!.value.toInt() - 1] to uvs[vertex.groups[it + 1]!!.value.toInt() - 1]
                }
                val (v1, uv1) = face1
                val (v2, uv2) = face2
                val (v3, uv3) = face3
                quads += BakedQuad(
                    intArrayOf(
                        // face 1
                        v1.x.toBits(),
                        v1.y.toBits(),
                        v1.z.toBits(),
                        0,
                        uv1.u.toBits(),
                        uv1.v.toBits(),
                        0,
                        0,
                        // face 1 again
                        v1.x.toBits(),
                        v1.y.toBits(),
                        v1.z.toBits(),
                        0,
                        uv1.u.toBits(),
                        uv1.v.toBits(),
                        0,
                        0,
                        // face 2
                        v2.x.toBits(),
                        v2.y.toBits(),
                        v2.z.toBits(),
                        0,
                        uv2.u.toBits(),
                        uv2.v.toBits(),
                        0,
                        0,
                        // face 3
                        v3.x.toBits(),
                        v3.y.toBits(),
                        v3.z.toBits(),
                        0,
                        uv3.u.toBits(),
                        uv3.v.toBits(),
                        0,
                        0,
                    ),
                    -1,
                    Direction.UP, // todo???
                    null,         // "
                    true,
                )
                return@forEachLine
            }
        }
        return quads
    }
}