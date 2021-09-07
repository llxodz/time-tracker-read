package ru.llxodz.timetracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.llxodz.timetracker.helper.toHMS
import ru.llxodz.timetracker.list.ListAdapter
import ru.llxodz.timetracker.model.Task
import ru.llxodz.timetracker.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class  MainActivity : AppCompatActivity() {

    private lateinit var mTaskViewModel: TaskViewModel

    companion object {
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "channelID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // RecyclerView
        val adapter = ListAdapter()
        val recyclerView = recyclerview

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

//        // TaskView model
        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        mTaskViewModel.readAllData.observe(this, { tasks ->
            adapter.setData(tasks)
            var sumTimes: Long = 0
            tasks.forEach { i ->
                sumTimes += i.time
            }

            all_time_tv.text = sumTimes.toHMS
        })

        all_time_tv.setOnClickListener {
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)

            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Читайте!")
                .setContentText("Вы читаете")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this)) {
                notify(NOTIFICATION_ID, builder.build()) // посылаем уведомление
            }
        }


        total_time_tv.setOnClickListener {
            mTaskViewModel.deleteAllTasks()
        }
    }
}