package ru.llxodz.timetracker.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "tasks_table")
data class Task(

    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val time: Long,
    val date: String,
    val status: String
) : Parcelable