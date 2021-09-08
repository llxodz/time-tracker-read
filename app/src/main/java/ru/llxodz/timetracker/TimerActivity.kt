package ru.llxodz.timetracker

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.transition.Visibility
import android.util.Log
import android.view.View.*
import android.widget.Chronometer
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet.GONE
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_timer.*
import ru.llxodz.timetracker.helper.toDMY
import ru.llxodz.timetracker.model.Task
import ru.llxodz.timetracker.viewmodel.TaskViewModel

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
            Log.d("TAG", "$timeInDatabase")
        }

        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        button_start_chronometer.setOnClickListener {
            chronometer.base = SystemClock.elapsedRealtime()
            chronometer.start()
            button_start_chronometer.visibility = INVISIBLE
            button_stop_chronometer.visibility = VISIBLE
        }

        button_stop_chronometer.setOnClickListener {
            chronometer.stop()
            insertDataToDatabase()
            button_stop_chronometer.visibility = INVISIBLE
            button_start_chronometer.visibility = VISIBLE
        }

        button_back.setOnClickListener {
            if (timeInDatabase > 0) {
                insertDataToDatabase()
                finish()
            } else {
                finish()
            }
        }

        button_settings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun insertDataToDatabase() {
        val time = timeInDatabase
        val date = System.currentTimeMillis().toDMY
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
}
