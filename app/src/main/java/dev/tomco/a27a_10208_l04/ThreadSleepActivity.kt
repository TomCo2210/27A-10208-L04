package dev.tomco.a27a_10208_l04

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textview.MaterialTextView
import dev.tomco.a27a_10208_l04.utilities.Constants
import dev.tomco.a27a_10208_l04.utilities.TimeFormatter

class ThreadSleepActivity : AppCompatActivity() {


    private lateinit var main_LBL_time: MaterialTextView

    private lateinit var main_FAB_start: ExtendedFloatingActionButton

    private lateinit var main_FAB_stop: ExtendedFloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()

        initViews()

    }

    private fun findViews() {
        main_LBL_time = findViewById(R.id.main_LBL_time)
        main_FAB_start = findViewById(R.id.main_FAB_start)
        main_FAB_stop = findViewById(R.id.main_FAB_stop)
    }

    private fun initViews() {
        main_FAB_start.setOnClickListener { startTimer() }
        main_FAB_stop.setOnClickListener { stopTimer(0L) }
    }

    fun stopTimer(elapsed: Long) {
        showTimer(elapsed)
    }

    private fun startTimer() {
        for (seconds in 1..Constants.Timer.SLEEP_DEMO_SECONDS){
            Thread.sleep(Constants.Timer.DELAY)
            Log.d("Timer Sleep:", "$seconds")
            showTimer(seconds * Constants.Timer.DELAY)
        }
    }

    private fun showTimer(elapsed: Long) {
        main_LBL_time.text = TimeFormatter.formatTime(elapsed)
    }
}
