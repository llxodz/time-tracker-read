package ru.llxodz.timetracker.activities

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Chronometer
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_timer.*
import ru.llxodz.timetracker.R
import ru.llxodz.timetracker.helper.Constants
import ru.llxodz.timetracker.helper.toDMY
import ru.llxodz.timetracker.helper.toHMS
import ru.llxodz.timetracker.model.Task
import ru.llxodz.timetracker.model.TimerEvent
import ru.llxodz.timetracker.service.TimerService
import ru.llxodz.timetracker.viewmodel.TaskViewModel

class TimerActivity : AppCompatActivity() {

    private lateinit var mTaskViewModel: TaskViewModel
    private var writeToDatabase: Boolean = false
    private var timeCycle: Long = 1500000
    private var timeInDatabase: Long = 0
    private var isTimerRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        button_start_chronometer.setOnClickListener {
            toggleTimer()
            button_start_chronometer.visibility = INVISIBLE
            button_stop_chronometer.visibility = VISIBLE
        }

        button_stop_chronometer.setOnClickListener {
            insertDataToDatabase()
            writeToDatabase = true
            toggleTimer()
            button_stop_chronometer.visibility = INVISIBLE
            button_start_chronometer.visibility = VISIBLE
        }

        button_back.setOnClickListener {
            toggleTimer()
            if (timeInDatabase > 0 && !writeToDatabase) {
                insertDataToDatabase()
                finish()
            } else {
                finish()
            }
        }

        button_settings.setOnClickListener {
            toggleTimer()
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        setObservers()

        TimerService.timerInMillis.observe(this, Observer {
            tv_chronometer.text = it.toHMS
            val correctTime = (it.toFloat() / timeCycle.toFloat()) * 100
            circular_progress_bar.setProgressWithAnimation(correctTime)
            timeInDatabase = it
        })
    }

    private fun toggleTimer() {
        if (isTimerRunning) {
            sendCommandToService(Constants.ACTION_STOP_SERVICE)
        } else {
            sendCommandToService(Constants.ACTION_START_SERVICE)
        }
    }

    private fun setObservers() {
        TimerService.timerEvent.observe(this, Observer {
            isTimerRunning = when (it) {
                is TimerEvent.START -> {
                    true
                }
                is TimerEvent.END -> {
                    false
                }
            }
        })
    }

    private fun insertDataToDatabase() {
        val time = timeInDatabase
        val date = System.currentTimeMillis().toDMY
        val status = if (time >= timeCycle) {
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

    private fun sendCommandToService(action: String) {
        startService(
            Intent(this, TimerService::class.java).apply {
                this.action = action
            }
        )
    }
}
