package com.study.vhra.introgl

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Square {
    companion object {
        const val COORDS_PER_VERTEX = 3
        const val BYTES_PER_VERTEX = 4
    }

    private var vertices = floatArrayOf(
        -0.5f,  0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        0.5f,  0.5f, 0.0f
    )

    private var textureCoods= floatArrayOf(
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f,
        1.0f, 0.0f
    )

    private val drawOrder = shortArrayOf(
        0, 1, 2, 0, 2, 3) // order to draw vertices

    // initialize vertex byte buffer for shape coordinates
    val vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(vertices.size * BYTES_PER_VERTEX).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(vertices)
                position(0)
            }
        }

    val coordTextureBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(textureCoods.size * BYTES_PER_VERTEX).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(textureCoods)
                position(0)
            }
        }

    // initialize byte buffer for the draw list
    val drawListBuffer: ShortBuffer =
        ByteBuffer.allocateDirect(drawOrder.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(drawOrder)
                position(0)
            }
        }

    val indexNumber: Int = drawOrder.size
    val vertexCount: Int = vertices.size / COORDS_PER_VERTEX
    val vertexStride: Int = COORDS_PER_VERTEX * BYTES_PER_VERTEX
}
