package com.study.vhra.cardview.model

import android.opengl.GLES20.*
import android.opengl.Matrix
import android.os.SystemClock
import android.util.Log
import com.study.vhra.cardview.R
import com.study.vhra.cardview.utils.BufferLoader
import com.study.vhra.cardview.utils.TextureLoader
import com.study.vhra.cardview.utils.TimeCounter

class CardModel : GraphicModel {
    companion object {
        const val BYTES_PER_FLOAT = 4
        const val BYTES_PER_INT = 2
    }

    private var vertices = floatArrayOf(
       // front
       -0.5f,  0.5f, 0.0f,
       -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        0.5f,  0.5f, 0.0f,
       // back
       -0.5f,  0.5f, 0.0f,
       -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f,
        0.5f,  0.5f, 0.0f
    )

    private var textureCoods = floatArrayOf(
        // front
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f,
        1.0f, 0.0f,
        // back
        0.0f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f,
        1.0f, 0.0f
    )

    private var textureIndices = floatArrayOf(
        // front
        0f, 0f, 0f, 0f,
        // back
        1f, 1f, 1f, 1f
    )

    private val indices = shortArrayOf(
        // front
        0, 2, 1, 0, 3, 2,
        // back
        4, 5, 6, 4, 6, 7
    )

    private var vertexBuffer: Int = 0
    private var coordTextureBuffer: Int = 0
    private var textureIndicesBuffer: Int = 0
    private var indexBuffer: Int = 0

    private var angle: Float = 0.0f
    private val rotateSpeed = 45f/1000f
    private val timeCounter: TimeCounter by lazy { TimeCounter().also { it.start() } }

    private val mProjectionMatrix: FloatArray by lazy {
        FloatArray(16).also { Matrix.setIdentityM(it, 0) }
    }

    private val mMatrix: FloatArray by lazy {
        FloatArray(16).also { Matrix.setIdentityM(it, 0) }
    }

    override fun loadModel(bufferLoader: BufferLoader, textureLoader: TextureLoader) {
        vertexBuffer = bufferLoader.createArrayBuffer(vertices, BYTES_PER_FLOAT)
        coordTextureBuffer = bufferLoader.createArrayBuffer(textureCoods, BYTES_PER_FLOAT)
        indexBuffer = bufferLoader.createElementArrayBuffer(indices, BYTES_PER_INT)
        textureIndicesBuffer = bufferLoader.createArrayBuffer(textureIndices, BYTES_PER_FLOAT)

        // Set the active texture unit to texture unit 0.
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_2D, textureLoader.loadTexture(R.drawable.image))

        // Set the active texture unit to texture unit 1.
        glActiveTexture(GL_TEXTURE1)
        glBindTexture(GL_TEXTURE_2D, textureLoader.loadTexture(R.drawable.card))
    }

    override fun onAttach(shader: ShaderProgram) {
        shader.getAttribute("vPosition").also {
            glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer)
            glVertexAttribPointer(it, 3, GL_FLOAT, false, 3 * BYTES_PER_FLOAT, 0)
        }

        shader.getAttribute("aTexCoordinate").also {
            glBindBuffer(GL_ARRAY_BUFFER, coordTextureBuffer)
            glVertexAttribPointer(it, 2, GL_FLOAT, false, 2 * BYTES_PER_FLOAT, 0)
        }

        shader.getAttribute("aTextureIndex").also {
            glBindBuffer(GL_ARRAY_BUFFER, textureIndicesBuffer)
            glVertexAttribPointer(it, 1, GL_FLOAT, false, 1 * BYTES_PER_FLOAT, 0)
        }

        shader.getUniform("uMatrix").also {
            glUniformMatrix4fv(it, 1, false, mMatrix, 0)
        }

        shader.getUniform("uTexture[0]").also {
            glUniform1i(it, 0)
        }

        shader.getUniform("uTexture[1]").also {
            glUniform1i(it, 1)
        }
    }

    override fun onDetach(shaderProgram: ShaderProgram) {

    }

    override fun onUpdate() {
        angle += (rotateSpeed * timeCounter.getTime())

        Matrix.setIdentityM(mMatrix, 0)
        Matrix.translateM(mMatrix, 0, 0f, 0f, -2f)
        Matrix.rotateM(mMatrix, 0, angle, 0f, 1f, 0f)

        Matrix.multiplyMM(mMatrix,0, mProjectionMatrix, 0, mMatrix, 0)
    }

    override fun onDraw() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBuffer)
        glDrawElements(GL_TRIANGLES, indices.size, GL_UNSIGNED_SHORT, 0)
    }

    override fun onSurfaceChanged(width: Int, height: Int) {
        Matrix.setIdentityM(mProjectionMatrix, 0)
        val ratio: Float = width.toFloat() / height.toFloat()
        Matrix.perspectiveM(mProjectionMatrix, 0, 45.0f, ratio, 0.1f, 1000.0f)
    }
}