package com.study.vhra.cardview.utils

import android.os.SystemClock

class TimeCounter {
    var lastMarkedTime: Long = 0

    fun start() {
        lastMarkedTime = 0
    }

    fun getTime(): Long {
        val currentTime: Long = SystemClock.uptimeMillis()
        if (lastMarkedTime == 0L) lastMarkedTime = currentTime
        val result = (currentTime - lastMarkedTime)
        lastMarkedTime = currentTime
        return result
    }
}