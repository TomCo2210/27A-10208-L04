package dev.tomco.a27a_10208_l04
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textview.MaterialTextView
import dev.tomco.a27a_10208_l04.utilities.Constants
import dev.tomco.a27a_10208_l04.utilities.TimeFormatter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class KotlinCoroutinesActivity : AppCompatActivity() {

    private lateinit var main_LBL_time: MaterialTextView

    private lateinit var main_FAB_start: ExtendedFloatingActionButton

    private lateinit var main_FAB_stop: ExtendedFloatingActionButton

    // Nullable, not lateinit: pressing Stop before Start must not crash
    private var timerJob: Job? = null

    private var startTime: Long = 0
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
        if (timerOn)
            startTicking()
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
        updateTimerUI()
        main_FAB_start.setOnClickListener { startTimer() }
        main_FAB_stop.setOnClickListener { stopTimer() }
    }

    private fun updateTimerUI() {
        val elapsed = if (timerOn) SystemClock.elapsedRealtime() - startTime else 0L
        main_LBL_time.text = TimeFormatter.formatTime(elapsed)
    }

    private fun startTimer() {
        if (!timerOn) {
            timerOn = true
            startTime = SystemClock.elapsedRealtime()
            startTicking()
        }
    }

    private fun stopTimer() {
        timerOn = false
        timerJob?.cancel()
        timerJob = null
    }

    private fun startTicking() {
        timerJob?.cancel()
        timerJob = lifecycleScope.launch {
            // lifecycleScope alone keeps running in the background until onDestroy.
            // repeatOnLifecycle pauses the loop below onStop and restarts it on onStart.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (isActive) {
                    Log.d("Timer Coroutine", "${SystemClock.elapsedRealtime()}")
                    updateTimerUI()
                    delay(Constants.Timer.DELAY)
                }
            }
        }
    }
}
