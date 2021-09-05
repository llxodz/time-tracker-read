package ru.llxodz.timetracker

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.Chronometer
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_timer.*
import ru.llxodz.timetracker.model.Task
import ru.llxodz.timetracker.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

class TimerActivity : AppCompatActivity() {

    private lateinit var mTaskViewModel: TaskViewModel
    private lateinit var chronometer: Chronometer
    private var timeCycle: Long = 10000
    private var timeInDatabase: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        chronometer = findViewById(R.id.chronometer)
        chronometer.format = "00:%s"
        chronometer.setOnChronometerTickListener { cArg ->
            val elapsedMillis = SystemClock.elapsedRealtime() - cArg.base
            val correctTime = (elapsedMillis.toFloat() / timeCycle.toFloat()) * 100
            circular_progress_bar.setProgressWithAnimation(correctTime)
            if (elapsedMillis > 3600000L) {
                cArg.format = "0%s"
            } else {
                cArg.format = "00:%s"
            }
            timeInDatabase = elapsedMillis
        }

        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        button_start_chronometer.setOnClickListener {
            chronometer.base = SystemClock.elapsedRealtime()
            chronometer.start()
        }

        button_back.setOnClickListener {
            insertDataToDatabase()
            finish()
        }
    }

    private fun insertDataToDatabase() {
        val time = timeInDatabase
        val date = 222
        val status = if (time > timeCycle || time == timeCycle) {
            "Выполнено"
        } else {
            "Не выполнено"
        }

        val task = Task(
            0,
            time,
            date,
            status
        )
        mTaskViewModel.addTask(task)
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd.MM.yy")
        return format.format(date)
    }
}