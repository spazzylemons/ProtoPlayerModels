package spazzylemons.protoplayermodels.model

import net.minecraft.client.util.math.Vector3f
import java.io.Reader
import java.nio.charset.Charset

object OBJLoader {
    fun load(data: Reader): QuadModel {
        val vertices = mutableListOf<Vector3f>()
        val uvs = mutableListOf<UVCoordinate>()
        val normals = mutableListOf<Vector3f>()
        val faces = mutableListOf<Face>()

        data.forEachLine { line ->
            when {
                line.startsWith("v ") -> {
                    val (x, y, z) = line.split(' ').drop(1).map(String::toFloat)
                    vertices += Vector3f(x, -y, -z) // Flip some coordinates?
                }
                line.startsWith("vt ") -> {
                    val (u, v) = line.split(' ').drop(1).map(String::toFloat)
                    uvs += UVCoordinate(u, -v) // Flip V coordinate
                }
                line.startsWith("vn ") -> {
                    val (x, y, z) = line.split(' ').drop(1).map(String::toFloat)
                    normals += Vector3f(x, -y, -z) // Flip some coordinates?
                }
                line.startsWith("f ") -> {
                    val normal = Vector3f()
                    val (a, b, c, d) = line.split(' ').drop(1).map {
                        val (vi, uvi, ni) = it.split('/').map { i -> i.toInt() - 1 }
                        normal.add(normals[ni])
                        Vertex(vertices[vi], uvs[uvi])
                    }
                    normal.normalize()
                    faces += Face(a, b, c, d, normal)
                }
            }
        }
        return QuadModel(faces.toTypedArray())
    }

    fun load(resourceName: String) = this::class.java.classLoader.getResource(resourceName)?.openStream()?.use {
        load(it.reader(Charset.defaultCharset()))
    }
}