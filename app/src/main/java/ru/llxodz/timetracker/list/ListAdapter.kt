package ru.llxodz.timetracker.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.custom_row.view.*
import ru.llxodz.timetracker.R
import ru.llxodz.timetracker.model.Task
import java.text.SimpleDateFormat
import java.util.*

class ListAdapter() : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private var taskList = emptyList<Task>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.custom_row, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTask = taskList[position]

        holder.itemView.tv_time_in_date.text = convertLongToTime(currentTask.time)
        holder.itemView.tv_date_task.text = currentTask.date
        holder.itemView.tv_status_task.text = currentTask.status
    }

    fun setData(task: List<Task>) {
        this.taskList = task
        notifyDataSetChanged()
    }

    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("mm:ss")
        return format.format(date)
    }
}