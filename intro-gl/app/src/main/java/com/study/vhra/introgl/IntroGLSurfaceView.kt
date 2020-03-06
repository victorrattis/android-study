package com.study.vhra.introgl

import android.content.Context
import android.opengl.GLSurfaceView

class IntroGLSurfaceView(context: Context) : GLSurfaceView(context) {
    private val renderer: IntroGLRenderer

    init {
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        renderer = IntroGLRenderer(context)

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)
    }
}