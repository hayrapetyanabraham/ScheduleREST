package com.smartclick.custompushnotifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.smartclick.custompushnotifications.receivers.MyAlarmReceiver
import com.smartclick.custompushnotifications.receivers.MySimpleReceiver
import com.smartclick.custompushnotifications.services.MySimpleService

class MainActivity : AppCompatActivity() {
    private var alarmPendingIntent: PendingIntent? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkForMessage();
    }

    override fun onStart() {
        super.onStart()
        initServices()
    }

    fun initServices(){
        onSimpleService()
        onStartAlarm()
    }


    fun onSimpleService() {
        // Construct our Intent specifying the Service
        val i = Intent(this, MySimpleService::class.java)
        // Add extras to the bundle
        i.putExtra("foo", "bar")
        i.putExtra("receiver", MySimpleReceiver.setupServiceReceiver(this@MainActivity))
        // Start the service
        MySimpleService.enqueueWork(this, i)
    }


    fun onStartAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        val intent = Intent(applicationContext, MyAlarmReceiver::class.java)
        // Create a PendingIntent to be triggered when the alarm goes off
        alarmPendingIntent = PendingIntent.getBroadcast(
            this, MyAlarmReceiver.REQUEST_CODE,
            intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        // Setup periodic alarm every 5 seconds
        val firstMillis =
            System.currentTimeMillis() // first run of alarm is immediate
        val intervalMillis =
            1000 // as of API 19, alarm manager will be forced up to 60000 to save battery
        val alarm =
            this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // See https://developer.android.com/training/scheduling/alarms.html
        alarm.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            firstMillis,
            intervalMillis.toLong(),
            alarmPendingIntent
        )
    }

    fun onStopAlarm() {
        val alarm =
            this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (alarmPendingIntent != null) {
            alarm.cancel(alarmPendingIntent)
        }
    }

    // Checks to see if service passed in a message
    private fun checkForMessage() {
        val message = intent.getStringExtra("message")
        if (message != null) {
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

}