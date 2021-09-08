package ru.llxodz.timetracker.model

sealed class TimerEvent {
    object START : TimerEvent()
    object END : TimerEvent()
}