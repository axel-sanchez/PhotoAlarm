package com.example.photoalarm.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * @author Axel Sanchez
 */
class ChronometerWorker(appContext: Context, workerParams: WorkerParameters):
    Worker(appContext, workerParams) {
    override fun doWork(): Result {



        return Result.success()
    }
}