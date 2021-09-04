package ru.llxodz.timetracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
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

//        // TaskView model
        mTaskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        mTaskViewModel.readAllData.observe(this, { task ->
            adapter.setData(task)
        })

        all_time_tv.setOnClickListener {
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }

        total_time_tv.setOnClickListener {
            mTaskViewModel.deleteAllTasks()
        }
    }
}