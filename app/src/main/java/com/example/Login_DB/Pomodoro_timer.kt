import android.media.MediaPlayer
import android.os.CountDownTimer
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.view.iterator
import com.example.Login_DB.R

class Pomodoro_timer(
    private val callback: PomodoroCallback,
    private val notesLayout: LinearLayout
) {
    private var isRunning = false

    private lateinit var countDownTimer: CountDownTimer
    private lateinit var mediaPlayer: MediaPlayer

    private var isPomodoroTimer = false
    private var isTimerRunning = false
    private var timeLeftInMillis: Long = 10000

    private val countdownInterval: Long = 1000
    private val workDuration: Long = 10000
    private val breakDuration: Long = 5000

    interface PomodoroCallback {
        fun onTimerTick(timeLeftInMillis: Long)
        fun onTimerFinish()
    }

    init {
            mediaPlayer = MediaPlayer.create(notesLayout.context, R.raw.sound)

    }

    fun startPomodoro() {
        if (!isTimerRunning) {
            countDownTimer = object : CountDownTimer(timeLeftInMillis, countdownInterval) {
                override fun onTick(millisUntilFinished: Long) {
                    timeLeftInMillis = millisUntilFinished
                    callback.onTimerTick(timeLeftInMillis)
                }

                override fun onFinish() {
                    isTimerRunning = false
                    isRunning = false // Add this line to reset isRunning flag
                    callback.onTimerFinish()
                    playSound()
                }
            }.start()

            isTimerRunning = true
            isRunning = true // Add this line to set isRunning flag when the timer starts
        }
    }

    fun pauseTimer() {
        countDownTimer.cancel()
        isTimerRunning = false
        isRunning = false
    }


    fun resetTimer() {
        countDownTimer.cancel()
        isTimerRunning = false
        isRunning = false
        isRunning = false
        timeLeftInMillis = if (isPomodoroTimer) workDuration else breakDuration
        callback.onTimerTick(timeLeftInMillis)
    }


    fun isTimerRunning(): Boolean {
        return isRunning
    }

    fun addTaskDetails(note: String) {
        addNotes(note)
    }

    fun removeCheckedNotes() {
        val iterator = notesLayout.iterator()

        while (iterator.hasNext()) {
            val noteLayout = iterator.next() as? LinearLayout
            val checkBox = noteLayout?.getChildAt(0) as? CheckBox

            if (checkBox?.isChecked == true) {
                iterator.remove()
            }
        }
    }

    private fun addNotes(note: String) {
        val newNoteLayout = createNoteLayout(note)
        notesLayout.addView(newNoteLayout)
        val checkBox = newNoteLayout.getChildAt(0) as CheckBox
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                notesLayout.removeView(newNoteLayout)
                removeCheckedNotes()
            }
        }
    }

    fun setPomodoroTimer() {
        if (!isTimerRunning) {
            isPomodoroTimer = true
            timeLeftInMillis = workDuration
            callback.onTimerTick(timeLeftInMillis)
        }
    }

    fun setBreakTimer() {
        if (!isTimerRunning) {
            isPomodoroTimer = false
            timeLeftInMillis = breakDuration
            callback.onTimerTick(timeLeftInMillis)
        }
        isRunning = true
    }

    fun playSound() {
        mediaPlayer.start()
    }

    private fun createNoteLayout(note: String): LinearLayout {
        val noteLayout = LinearLayout(notesLayout.context)
        noteLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        noteLayout.orientation = LinearLayout.HORIZONTAL

        val checkBox = CheckBox(notesLayout.context)
        checkBox.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val textView = TextView(notesLayout.context)
        textView.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        textView.text = note
        textView.textSize = 18f

        noteLayout.addView(checkBox)
        noteLayout.addView(textView)

        return noteLayout
    }
}
