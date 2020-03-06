package com.study.vhra.introgl

import android.content.Context
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.opengl.Matrix
import java.nio.FloatBuffer


class IntroGLRenderer(private var mContext: Context) : GLSurfaceView.Renderer {
    private lateinit var square: Square

    private var mShaderProgram: Int = 0
    private var positionHandle: Int = 0

    /** This will be used to pass in the texture.  */
    private var mTextureUniformHandle: Int = 0

    /** This will be used to pass in model texture coordinate information.  */
    private var mTextureCoordinateHandle: Int = 0

    private var mTextureDataHandle: Int = 0

    private val vertexShaderCode =
            "attribute vec3 vPosition;" +
            "attribute vec2 a_TexCoordinate;" +
            "varying vec2 v_TexCoordinate; " +
            "uniform mat4 u_tmatrix;" +
            "void main() {" +
                "gl_Position = u_tmatrix * vec4(vPosition, 1.0);" +
                "v_TexCoordinate = a_TexCoordinate;" +
            "}"

    private val fragmentShaderCode =
        "precision mediump float;" +
        "uniform sampler2D u_Texture;" +
        "uniform vec2 textVector;" +
        "varying vec2 v_TexCoordinate;" +
        "void main() {" +
            "gl_FragColor = vec4(texture2D(u_Texture, v_TexCoordinate * textVector).rgb, 1.0);" +
        "}"

    private var mMatrix: FloatArray? = null
    private var mMatrixHandle = 0

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        // Set the background frame color
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f)
        loadShaderProgram()
        square = Square()

        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mShaderProgram)

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(mShaderProgram, "vPosition").also {
            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)
        }

        mMatrix = FloatArray(16)
        Matrix.setIdentityM(mMatrix, 0)
        Matrix.scaleM(mMatrix, 0, 2f, 2f, 2f)


        mMatrixHandle = GLES20.glGetUniformLocation(mShaderProgram, "u_tmatrix")
        GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mMatrix, 0)

        val mOpacityHandle = GLES20.glGetUniformLocation(mShaderProgram, "textVector")
        GLES20.glUniform2f(mOpacityHandle, -1.0f, -1.0f)

        mTextureUniformHandle = GLES20.glGetUniformLocation(mShaderProgram, "u_Texture")

        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mShaderProgram, "a_TexCoordinate").also {
            // Enable a handle to the triangle vertices
            GLES20.glEnableVertexAttribArray(it)
        }

        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0)

        mTextureDataHandle = loadTexture(mContext, R.drawable.image)
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureDataHandle)
    }

    override fun onDrawFrame(gl: GL10) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(
            positionHandle,
            Square.COORDS_PER_VERTEX,
            GLES20.GL_FLOAT,
            false,
            square.vertexStride,
            square.vertexBuffer
        )

        GLES20.glVertexAttribPointer(
            mTextureCoordinateHandle,
            2,
            GLES20.GL_FLOAT,
            false,
            2 * 4,
            square.coordTextureBuffer
        )

        // Draw the triangle
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, square.vertexCount)

        GLES20.glDrawElements(
            GLES20.GL_TRIANGLES,
            square.indexNumber,
            GLES20.GL_UNSIGNED_SHORT,
            square.drawListBuffer)

        // Disable vertex array
//        GLES20.glDisableVertexAttribArray(positionHandle)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        gl.glViewport(0, 0, width, height)

        Matrix.setIdentityM(mMatrix, 0)
        val ratio: Float = width.toFloat() / height.toFloat()
        Matrix.frustumM(mMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)

        Matrix.translateM(mMatrix, 0, 0f, 0f, -5f)
        val angle: Float = 0f * 360 / Math.PI.toFloat()
        Matrix.rotateM(mMatrix, 0, angle, 0f, 1f, 0f)

        GLES20.glUniformMatrix4fv(mMatrixHandle, 1, false, mMatrix, 0)
    }

    private fun loadShaderProgram() {
        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
        mShaderProgram = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
        }
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES20.glCreateShader(type).also { shader ->
            GLES20.glShaderSource(shader, shaderCode)
            GLES20.glCompileShader(shader)
        }
    }

    fun loadTexture(context: Context, resourceId: Int): Int {
        val textureHandle = IntArray(1)

        GLES20.glGenTextures(1, textureHandle, 0)

        if (textureHandle[0] != 0) {
            val options = BitmapFactory.Options()
            options.inScaled = false   // No pre-scaling

            // Read in the resource
            val bitmap = BitmapFactory.decodeResource(context.resources, resourceId, options)

            // Bind to the texture in OpenGL
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])

            // Set filtering
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_NEAREST
            )
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_NEAREST
            )

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle()
        }

        if (textureHandle[0] == 0) {
            throw RuntimeException("Error loading texture.")
        }

        return textureHandle[0]
    }
}