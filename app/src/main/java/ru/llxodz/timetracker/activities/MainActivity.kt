package ru.llxodz.timetracker.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.llxodz.timetracker.R
import ru.llxodz.timetracker.helper.toHMS
import ru.llxodz.timetracker.list.ListAdapter
import ru.llxodz.timetracker.viewmodel.TaskViewModel

class  MainActivity : AppCompatActivity() {

    private lateinit var mTaskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // RecyclerView
        val adapter = ListAdapter()
        val recyclerView = recyclerview

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // TaskView model
        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        mTaskViewModel.readAllData.observe(this, { tasks ->
            adapter.setData(tasks)
            var sumTimes: Long = 0
            tasks.forEach { i ->
                sumTimes += i.time
            }

            all_time_tv.text = sumTimes.toHMS
        })

        button_clock.setOnClickListener {
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }


        total_time_tv.setOnClickListener {
            mTaskViewModel.deleteAllTasks()
        }
    }
}