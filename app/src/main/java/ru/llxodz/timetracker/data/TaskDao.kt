package ru.llxodz.timetracker.data

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.llxodz.timetracker.model.Task

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM tasks_table")
    suspend fun deleteAllTasks()

    @Query("SELECT * FROM tasks_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Task>>
}