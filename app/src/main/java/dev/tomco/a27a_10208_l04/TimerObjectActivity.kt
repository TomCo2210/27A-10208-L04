package dev.tomco.a27a_10208_l04

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textview.MaterialTextView
import dev.tomco.a27a_10208_l04.utilities.Constants
import dev.tomco.a27a_10208_l04.utilities.TimeFormatter
import java.util.Timer
import java.util.TimerTask

class TimerObjectActivity : AppCompatActivity() {


    private lateinit var main_LBL_time: MaterialTextView

    private lateinit var main_FAB_start: ExtendedFloatingActionButton

    private lateinit var main_FAB_stop: ExtendedFloatingActionButton

    private var timer: Timer? = null

    private var startTime: Long = 0L
    private var timerOn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        savedInstanceState?.let {
            startTime = it.getLong(Constants.StateKeys.START_TIME)
            timerOn = it.getBoolean(Constants.StateKeys.TIMER_ON)
        }
        findViews()
        initViews()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(Constants.StateKeys.START_TIME, startTime)
        outState.putBoolean(Constants.StateKeys.TIMER_ON, timerOn)


    }

    private fun findViews() {
        main_LBL_time = findViewById(R.id.main_LBL_time)
        main_FAB_start = findViewById(R.id.main_FAB_start)
        main_FAB_stop = findViewById(R.id.main_FAB_stop)
    }

    private fun initViews() {
        main_FAB_start.setOnClickListener { startTimer() }
        main_FAB_stop.setOnClickListener { stopTimer() }
    }

    override fun onStart() {
        super.onStart()
        if (timerOn)
            startTicking()
    }


    private fun startTimer() {
        if (!timerOn) {
            timerOn = true
            startTime = SystemClock.elapsedRealtime()
            startTicking()
        }
    }

    private fun startTicking() {
        stopTicking()
        timer = Timer().apply {
            schedule(
                object : TimerTask() {
                    override fun run() {
                        Log.d("Timer Object:", "${SystemClock.elapsedRealtime()}")
                        runOnUiThread{ updateTimerUI() }
                    }
                }, 0,
                Constants.Timer.DELAY
            )
        }
    }

    override fun onStop() {
        super.onStop()
        stopTicking()
    }

    fun stopTimer() {
        timerOn = false
        stopTicking()

    }

    private fun stopTicking() {
        timer?.cancel()
        timer = null
    }

    private fun updateTimerUI() {
        var elapsed = SystemClock.elapsedRealtime() - startTime
        main_LBL_time.text = TimeFormatter.formatTime(elapsed)
    }
}
