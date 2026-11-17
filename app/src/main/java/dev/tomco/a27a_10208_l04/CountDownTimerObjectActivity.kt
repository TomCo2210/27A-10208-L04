package dev.tomco.a27a_10208_l04

import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textview.MaterialTextView
import dev.tomco.a27a_10208_l04.utilities.Constants
import dev.tomco.a27a_10208_l04.utilities.TimeFormatter

class CountDownTimerObjectActivity : AppCompatActivity() {


    private lateinit var main_LBL_time: MaterialTextView

    private lateinit var main_FAB_start: ExtendedFloatingActionButton

    private lateinit var main_FAB_stop: ExtendedFloatingActionButton

    private var countDownTimer: CountDownTimer? = null

    private var endTime: Long = 0L
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
            endTime = it.getLong(Constants.StateKeys.END_TIME)
            timerOn = it.getBoolean(Constants.StateKeys.TIMER_ON)
        }
        findViews()
        initViews()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(Constants.StateKeys.END_TIME, endTime)
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
            startCountDown()
    }


    private fun startTimer() {
        if (!timerOn) {
            timerOn = true
            endTime = SystemClock.elapsedRealtime() + Constants.Timer.COUNT_DOWN_DURATION
            startCountDown()
        }
    }

    private fun startCountDown() {
        cancelCountDown()
        val remaining = endTime - SystemClock.elapsedRealtime()
        if (remaining <= 0) {
            onCountDownFinished()
            return
        }
        countDownTimer = object : CountDownTimer(remaining, Constants.Timer.DELAY) {
            override fun onFinish() {
                onCountDownFinished()
            }

            override fun onTick(millisUntilFinished: Long) {
                Log.d("Timer CountDown:", "$millisUntilFinished")
                updateTimerUI(millisUntilFinished)
            }
        }.start()
    }

    override fun onStop() {
        super.onStop()
        cancelCountDown()
    }

    fun stopTimer() {
        cancelCountDown()
    }

    private fun cancelCountDown() {
        countDownTimer?.cancel()
        countDownTimer = null
    }

    private fun onCountDownFinished() {
        timerOn = false
        countDownTimer = null
        updateTimerUI(0L)
        Toast.makeText(this, R.string.times_up, Toast.LENGTH_SHORT).show()
    }

    private fun updateTimerUI(millisLeft: Long) {
        val roundedUp = (millisLeft + 999) / 1000 * 1000
        main_LBL_time.text = TimeFormatter.formatTime(roundedUp)
    }
}
