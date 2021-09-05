package ru.llxodz.timetracker.repository

import androidx.lifecycle.LiveData
import ru.llxodz.timetracker.data.TaskDao
import ru.llxodz.timetracker.model.Task

class TaskRepository(private val taskDao: TaskDao) {

    val readAllData: LiveData<List<Task>> = taskDao.readAllData()

    suspend fun addTask(task: Task) {
        taskDao.addTask(task)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }
}