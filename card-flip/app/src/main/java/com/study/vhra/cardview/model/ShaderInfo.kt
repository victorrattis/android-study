package com.study.vhra.cardview.model

data class ShaderInfo(
    val vertexShaderSourceCode: String,
    val fragmentShaderSourceCode: String,
    var programId: Int = 0
)