package com.study.vhra.cardview

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import com.study.vhra.cardview.model.GraphicModel
import com.study.vhra.cardview.model.ShaderInfo
import com.study.vhra.cardview.model.ShaderProgram
import com.study.vhra.cardview.utils.BufferLoader
import com.study.vhra.cardview.utils.ShaderLoader
import com.study.vhra.cardview.utils.TextureLoader
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class CardViewGlRenderer(
    private val shaderLoader: ShaderLoader,
    private val bufferLoader: BufferLoader,
    private val textureLoader: TextureLoader,
    private val shaderInfo: ShaderInfo?,
    private val model: GraphicModel?
) : GLSurfaceView.Renderer {
    private var shaderProgram: ShaderProgram? = null

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        // Set the background frame color
        glClearColor(1.0f, 1.0f, 1.0f, 1.0f)

        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)
        glFrontFace(GL_CW)

        loadShaderProgram()
        model?.loadModel(bufferLoader, textureLoader)

        shaderProgram?.let {
            it.useProgram()
            model?.onAttach(it)
        }
    }

    override fun onDrawFrame(gl: GL10) {
        glClear(GL_COLOR_BUFFER_BIT)

        model?.onUpdate()

        shaderProgram?.let {
            model?.onAttach(it)
        }

        model?.onDraw()

        shaderProgram?.let {
            model?.onDetach(it)
        }
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        gl.glViewport(0, 0, width, height)

        model?.onSurfaceChanged(width, height)

        shaderProgram?.let {
            it.useProgram()
            model?.onAttach(it)
        }
    }

    private fun loadShaderProgram() {
        shaderInfo?.let {
            shaderProgram = shaderLoader.loadShader(it)
        }
    }
}