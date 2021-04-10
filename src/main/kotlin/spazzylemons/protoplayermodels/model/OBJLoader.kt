package spazzylemons.protoplayermodels.model

import net.minecraft.client.util.math.Vector3f
import java.io.Reader

object OBJLoader {
    // yes, it's regex. no i don't care, this won't run often enough for me to feel like optimizing it.
    private const val NUMBER = "(-?\\d+(?:\\.\\d+)?)"
    private const val FACE = "(\\d+)/(\\d+)/(\\d+)"

    private val VERTEX = Regex("v $NUMBER $NUMBER $NUMBER")
    private val UV = Regex("vt $NUMBER $NUMBER")
    private val NORMAL = Regex("vn $NUMBER $NUMBER $NUMBER")
    private val QUAD = Regex("f $FACE $FACE $FACE $FACE")

    fun load(resource: Reader): Model {
        val vertices = mutableListOf<Vector3f>()
        val uvs = mutableListOf<UVCoordinate>()
        val normals = mutableListOf<Vector3f>()
        val faces = mutableListOf<Face>()

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
                    -vertex.groups[2]!!.value.toFloat(), // flip v
                )
                return@forEachLine
            }
            NORMAL.find(line)?.let { vertex ->
                normals += Vector3f(
                    vertex.groups[1]!!.value.toFloat(),
                    -vertex.groups[2]!!.value.toFloat(), // flip y
                    vertex.groups[3]!!.value.toFloat(),
                )
                return@forEachLine
            }
            QUAD.find(line)?.let { vertex ->
                val v = (0..3).map { 1 + it * 3 }.map {
                    Vertex(
                        vertices[vertex.groups[it]!!.value.toInt() - 1],
                        uvs[vertex.groups[it + 1]!!.value.toInt() - 1],
                    ) to normals[vertex.groups[it + 2]!!.value.toInt() - 1]
                }
                val (v1, n1) = v[0]
                val (v2, n2) = v[1]
                val (v3, n3) = v[2]
                val (v4, n4) = v[3]
                faces += Face(
                    v1,
                    v2,
                    v3,
                    v4,
                    n1.copy().apply {
                        add(n2)
                        add(n3)
                        add(n4)
                        normalize()
                    }
                )
                return@forEachLine
            }
        }
        return Model(faces.toTypedArray())
    }
}