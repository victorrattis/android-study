package com.study.vhra.cardview.model

import android.opengl.GLES20.*

class ShaderProgram(
    val shaderInfo: ShaderInfo,
    private val programId: Int) {

    private var uniformMap: MutableMap<String, Int> = HashMap()
    private var attributeMap: MutableMap<String, Int> = HashMap()

    fun useProgram() {
        glUseProgram(programId)
    }

    fun getUniform(uniformName: String): Int {
        return if (uniformMap.containsKey(uniformName)) {
            uniformMap[uniformName]!!
        } else {
            glGetUniformLocation(programId, uniformName).also {
                uniformMap[uniformName] = it
            }
        }
    }

    fun getAttribute(attributeName: String): Int {
        return if (attributeMap.containsKey(attributeName)) {
            attributeMap[attributeName]!!
        } else {
            glGetAttribLocation(programId, attributeName).also {
                glEnableVertexAttribArray(it)
                attributeMap[attributeName] = it
            }
        }
    }

    fun desableAttributes() {
        attributeMap.values.forEach {
            glDisableVertexAttribArray(it)
        }
    }
}