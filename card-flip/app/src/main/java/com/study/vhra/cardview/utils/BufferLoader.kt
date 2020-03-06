package com.study.vhra.cardview.utils

import android.opengl.GLES20.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

class BufferLoader {
    fun createArrayBuffer(data: FloatArray, byteSize: Int): Int =
        IntArray(1).also {
            val buffer = ByteBuffer.allocateDirect(data.size * byteSize).run {
                order(ByteOrder.nativeOrder())
                asFloatBuffer().apply {
                    put(data)
                    position(0)
                }
            }

            glGenBuffers(it.size, it, 0)

            glBindBuffer(GL_ARRAY_BUFFER, it[0])
            glBufferData(
                GL_ARRAY_BUFFER, buffer.capacity() * byteSize,
                buffer, GL_STATIC_DRAW)
            glBindBuffer(GL_ARRAY_BUFFER, 0)
        }[0]

    fun createElementArrayBuffer(data: ShortArray, size: Int): Int =
        IntArray(1).also {
            val buffer = ByteBuffer.allocateDirect(data.size * size).run {
                order(ByteOrder.nativeOrder())
                asShortBuffer().apply {
                    put(data)
                    position(0)
                }
            }

            glGenBuffers(it.size, it, 0)

            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, it[0])
            glBufferData(
                GL_ELEMENT_ARRAY_BUFFER,
                buffer.capacity() * size,
                buffer,
                GL_STATIC_DRAW)
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
        }[0]
}