package com.study.vhra.cardview

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.Log
import com.study.vhra.cardview.model.CardModel
import com.study.vhra.cardview.model.ShaderInfo
import com.study.vhra.cardview.utils.BufferLoader
import com.study.vhra.cardview.utils.ShaderLoader
import com.study.vhra.cardview.utils.TextureLoader

class CardViewGLSurfaceView (context: Context): GLSurfaceView(context) {
    init {
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(CardViewGlRenderer(
            ShaderLoader(context),
            BufferLoader(),
            TextureLoader(context),
            ShaderInfo("raw://vertex_shader", "raw://fragment_shader"),
            CardModel()
        ))

        setOnClickListener {
            Log.d("devlog", "OnClickListener")
        }
    }
}