package com.study.vhra.cardview.model

import com.study.vhra.cardview.utils.BufferLoader
import com.study.vhra.cardview.utils.TextureLoader

interface GraphicModel {
    fun loadModel(bufferLoader: BufferLoader, textureLoader: TextureLoader)
    fun onAttach(shader: ShaderProgram)
    fun onDetach(shaderProgram: ShaderProgram)
    fun onUpdate()
    fun onDraw()
    fun onSurfaceChanged(width: Int, height: Int)
}