package ru.llxodz.timetracker.helper


val Long.toHMS: String
    get() {
        val seconds = this / 1000
        val toSeconds = seconds % 60
        val toMinutes = (seconds / 60) % 60
        val toHours = seconds / 60 / 60
        return String.format("%02d:%02d:%02d", toHours, toMinutes, toSeconds)
    }