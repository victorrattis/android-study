package com.study.vhra.cardview.utils

import android.content.Context
import android.opengl.GLES20.*
import com.study.vhra.cardview.model.ShaderInfo
import com.study.vhra.cardview.model.ShaderProgram
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader


class ShaderLoader(private val context: Context) {

    fun loadShader(shaderInfo: ShaderInfo) =
        ShaderProgram(shaderInfo, createShaderProgram(
            getSourceCode(context, shaderInfo.vertexShaderSourceCode),
            getSourceCode(context, shaderInfo.fragmentShaderSourceCode)))

    @Throws(IOException::class, RuntimeException::class)
    fun createShaderProgram(vertexShader: String, fragmentShader: String): Int =
        glCreateProgram().also { programId ->
            checkIfValidProgram(programId)
            attachProgram(programId,
                compileShader(vertexShader, GL_VERTEX_SHADER),
                compileShader(fragmentShader, GL_FRAGMENT_SHADER))
        }

    @Throws(IOException::class, RuntimeException::class)
    private fun compileShader(shaderSourceCode: String, shaderType: Int): Int =
        glCreateShader(shaderType).also { shaderId ->
            checkIfShader(shaderId)
            glShaderSource(shaderId, shaderSourceCode)
            glCompileShader(shaderId)
            checkShaderCompile(shaderId)
        }

    @Throws(RuntimeException::class)
    private fun attachProgram(programId: Int, vertexShader: Int, fragmentShader: Int) {
        glAttachShader(programId, vertexShader)
        glAttachShader(programId, fragmentShader)
        glLinkProgram(programId)
        checkAttachShader(programId)
    }

    private fun getSourceCode(context: Context, source: String): String =
        if (isRawFormat(source)) {
            val id = context.resources.getIdentifier(
                source.replace("raw://", ""),
                "raw",
                context.packageName)
            getSourceCode(context.resources.openRawResource(id))
        } else {
            source
        }

    private fun isRawFormat(text: String) = text.contains("raw://")

    private fun getSourceCode(inputStream: InputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        var line: String?
        val stringBuilder = StringBuilder()
        try {
            while (bufferedReader.readLine().also { line = it } != null) {
                stringBuilder.append(line).append('\n')
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return ""
        }
        return stringBuilder.toString()
    }

    @Throws(RuntimeException::class)
    private fun checkShaderCompile(shaderId: Int) {
        val status = IntArray(1)
        glGetShaderiv(shaderId, GL_COMPILE_STATUS, status, 0)
        if (status[0] == 0) {
            val log = glGetShaderInfoLog(shaderId)
            glDeleteShader(shaderId)
            throw java.lang.RuntimeException(log)
        }
    }

    @Throws(RuntimeException::class)
    private fun checkAttachShader(programId: Int) {
        val status = IntArray(1)
        glGetProgramiv(programId, GL_LINK_STATUS, status, 0)
        if (status[0] == 0) {
            val log = glGetProgramInfoLog(programId)
            glDeleteProgram(programId)
            throw RuntimeException(log)
        }
    }

    @Throws(RuntimeException::class)
    private fun checkIfValidProgram(programId: Int) {
        if (programId == 0) {
            throw RuntimeException("Failed to create the Shader Program!")
        }
    }

    @Throws(RuntimeException::class)
    private fun checkIfShader(shaderId: Int) {
        if (shaderId == 0) {
            throw RuntimeException("Failed to create Shader!")
        }
    }
}