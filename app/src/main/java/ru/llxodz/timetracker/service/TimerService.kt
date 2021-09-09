package ru.llxodz.timetracker.service

import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.NotificationManagerCompat.IMPORTANCE_LOW
import androidx.core.app.NotificationManagerCompat.from
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.llxodz.timetracker.R
import ru.llxodz.timetracker.activities.TimerActivity
import ru.llxodz.timetracker.helper.Constants
import ru.llxodz.timetracker.helper.toHMS
import ru.llxodz.timetracker.model.TimerEvent

class TimerService : LifecycleService() {

    companion object {
        val timerEvent = MutableLiveData<TimerEvent>()
        val timerInMillis = MutableLiveData<Long>()
    }

    private var isServiceStopped = false

    private lateinit var notificationManager: NotificationManagerCompat

    override fun onCreate() {
        super.onCreate()
        notificationManager = from(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                Constants.ACTION_START_SERVICE -> startForegroundService()

                Constants.ACTION_STOP_SERVICE -> stopForegroundService()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun initValues() {
        timerEvent.postValue(TimerEvent.END)
        timerInMillis.postValue(0L)
    }

    private fun startForegroundService() {
        timerEvent.postValue(TimerEvent.START)
        startTimer()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        startForeground(Constants.NOTIFICATION_ID, getNotificationBuilder().build())

        timerInMillis.observe(this, Observer {
            if (!isServiceStopped) {
                val builder = getNotificationBuilder().setContentText(
                    it.toHMS
                )
                notificationManager.notify(Constants.NOTIFICATION_ID, builder.build())
            }
        })
    }

    private fun stopForegroundService() {
        isServiceStopped = true
        initValues()
        notificationManager.cancel(Constants.NOTIFICATION_ID)
        stopForeground(true)
        stopSelf()
    }

    private fun startTimer() {
        val timerStarted = System.currentTimeMillis()
        CoroutineScope(Dispatchers.Main).launch {
            while (!isServiceStopped && timerEvent.value!! == TimerEvent.START) {
                val lapTime = System.currentTimeMillis() - timerStarted
                timerInMillis.postValue(lapTime)
                delay(50L)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    private fun getNotificationBuilder() =
        NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_stopwatch)
            .setContentTitle("Таймер запущен")
            .setContentText("00:00:00")
            .setContentIntent(getTimerActivityPendingIntent())

    private fun getTimerActivityPendingIntent() =
        PendingIntent.getActivity(
            this,
            143,
            Intent(this, TimerActivity::class.java).apply {
                this.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT
        )
}
