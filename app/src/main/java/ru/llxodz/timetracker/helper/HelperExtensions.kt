package ru.llxodz.timetracker.helper

import java.text.SimpleDateFormat
import java.util.*


val Long.toHMS: String
    get() {
        val seconds = this / 1000
        val toSeconds = seconds % 60
        val toMinutes = (seconds / 60) % 60
        val toHours = seconds / 60 / 60
        return String.format("%02d:%02d:%02d", toHours, toMinutes, toSeconds)
    }

val Long.toHM: String
    get() {
        val seconds = this / 1000
        val toSeconds = seconds % 60
        val toMinutes = (seconds / 60) % 60
        return String.format("%02d:%02d", toMinutes, toSeconds)
    }

val Long.toDMY: String
    get() {
        val date = Date(this)
        val format = SimpleDateFormat("dd.MM.yy")
        return format.format(date)
    }