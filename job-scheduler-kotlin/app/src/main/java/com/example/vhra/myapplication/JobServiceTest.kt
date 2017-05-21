package com.example.vhra.myapplication

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import android.util.Log
import android.os.Handler

const val TAG: String = "JobServiceTest"

class JobServiceTest : JobService() {
    private val WORK_DURATION_KEY = BuildConfig.APPLICATION_ID + ".WORK_DURATION_KEY"
    private var mMessage: String? = "default"

    //
    // Override Service methods
    //

    override fun onCreate() {
        Log.d(TAG, "onCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand[$startId]: $mMessage")
//        stopSelf(startId)
        return START_STICKY
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG, "onUnbind")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    override fun onRebind(intent: Intent?) {
        super.onRebind(intent)
        Log.d(TAG, "onRebind")
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d(TAG, "onTaskRemoved")
    }
    
    //
    // Override JobService methods
    //

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.i(TAG, "on start job: " + params?.jobId)

        val duration: Long? = params?.extras?.getLong(WORK_DURATION_KEY)
        mMessage = "on start job"

        val handler = Handler()
        if (duration != null) {
            handler.postDelayed({ // Runnable
                Log.i(TAG, "run thread")
                jobFinished(params, false)
            }, duration)
        }

        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.i(TAG, "on stop job: " + params?.jobId)
        mMessage = "on stop job"
        return true
    }
}