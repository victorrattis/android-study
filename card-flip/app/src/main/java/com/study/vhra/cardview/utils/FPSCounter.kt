package com.study.vhra.cardview.utils

import android.util.Log

class FPSCounter {
    private var startTime = System.nanoTime()
    private var frames = 0

    fun logFrame() {
        frames++
        if (System.nanoTime() - startTime >= 1000000000) {
            Log.d("FPSCounter", "fps: $frames")
            frames = 0
            startTime = System.nanoTime()
        }
    }
}