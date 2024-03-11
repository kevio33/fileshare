package com.kevin.singletasktest

import android.app.ActivityManager
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Activity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_2)
        setCustomTaskDescription()
    }
    private fun setCustomTaskDescription() {
        val taskDescription = ActivityManager.TaskDescription(
            "SecondActivity",
            BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        )
        setTaskDescription(taskDescription)
    }
}