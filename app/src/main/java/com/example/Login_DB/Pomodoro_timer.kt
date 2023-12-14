
package com.example.Login_DB

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.Login_DB.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class Pomodoro_timer : AppCompatActivity() {
    private lateinit var timerTextView: TextView
    private lateinit var statusTextView: TextView
    private lateinit var startButton: Button
    private lateinit var pauseButton: Button
    private lateinit var resetButton: Button
    private lateinit var breakButton: Button
    private lateinit var pomodoroButton: Button
    private lateinit var countDownTimer: CountDownTimer

    private lateinit var mediaPlayer: MediaPlayer
    private var isPomodoroTimer = false
    private var isTimerRunning = false
    private var timeLeftInMillis: Long = 10000

    private val countdownInterval: Long = 1000
    private val workDuration: Long = 10000
    private val breakDuration: Long = 5000

    //private val workDuration: Long = 25 * 60 * 1000 // 25 minutes
    //private val breakDuration: Long = 5 * 60 * 1000 // 5 minutes

    private lateinit var notesContainer: LinearLayout
    private lateinit var notesLayout: LinearLayout
    private lateinit var addNotesButton: Button
    private lateinit var taskDetails: String


    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timer_pomodoro)
        initializeViews()
        setClickListeners()
        // Initialize MediaPlayer
        //mediaPlayer = MediaPlayer.create(this, R.raw.sound)



    }

    private fun initializeViews() {
        timerTextView = findViewById(R.id.timerTextView)
        statusTextView = findViewById(R.id.statusTextView)
        startButton = findViewById(R.id.startButton)
        pauseButton = findViewById(R.id.pauseButton)
        resetButton = findViewById(R.id.resetButton)
        pomodoroButton = findViewById(R.id.pomodoroButton)
        breakButton = findViewById(R.id.breakButton)
        addNotesButton = findViewById(R.id.addNotesButton)
        notesContainer = findViewById(R.id.notesContainer)
        notesLayout = findViewById(R.id.notesLayout)
    }

    private fun setClickListeners() {
        startButton.setOnClickListener { startTimer() }
        pauseButton.setOnClickListener { pauseTimer() }
        resetButton.setOnClickListener { resetTimer() }
        pomodoroButton.setOnClickListener { setPomodoroTimer() }
        breakButton.setOnClickListener { setBreakTimer() }
        addNotesButton.setOnClickListener { showTaskDetailsDialog() }
    }

    private fun showTaskDetailsDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_dialog, null)
        builder.setView(dialogView)
        val input = dialogView.findViewById<EditText>(R.id.etTaskDetails)
        val btnOK = dialogView.findViewById<Button>(R.id.btnOK)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val alertDialog = builder.create()

        btnOK.setOnClickListener {
            val taskDetailsInput = input.text.toString().trim()
            if (taskDetailsInput.isNotEmpty()) {
                taskDetails = taskDetailsInput
                addNotes(taskDetails)
                removeCheckedNotes()
                alertDialog.dismiss()
            } else {
                showToast("Please enter a task.")
            }
        }

        btnCancel.setOnClickListener { alertDialog.dismiss() }
        alertDialog.show()
    }

    private fun addNotes(note: String) {
        val newNoteLayout = createNoteLayout(note)
        notesLayout.addView(newNoteLayout)
        val checkBox = newNoteLayout.getChildAt(0) as CheckBox
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                notesLayout.removeView(newNoteLayout)
                scrollNotesContainerToEnd()
            }
        }
    }

    private fun createNoteLayout(note: String): LinearLayout {
        val noteLayout = LinearLayout(this)
        noteLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        noteLayout.orientation = LinearLayout.HORIZONTAL
        noteLayout.gravity = Gravity.CENTER_VERTICAL
        val checkBox = CheckBox(this)
        checkBox.layoutParams = LinearLayout.LayoutParams(
            resources.getDimensionPixelSize(R.dimen.checkbox_width),
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        val textView = TextView(this)
        textView.layoutParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
        textView.text = note
        textView.textSize = 18f
        noteLayout.addView(checkBox)
        noteLayout.addView(textView)
        return noteLayout
    }

    private fun scrollNotesContainerToEnd() {
        val scrollView = findViewById<ScrollView>(R.id.notesScrollView)
        scrollView.post { scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
    }

    private fun removeCheckedNotes() {
        for (i in notesLayout.childCount - 1 downTo 0) {
            val noteLayout = notesLayout.getChildAt(i) as LinearLayout
            val checkBox = noteLayout.getChildAt(0) as CheckBox
            if (checkBox.isChecked) {
                notesLayout.removeViewAt(i)
            }
        }
    }

    private fun startTimer() {
        if (!isTimerRunning) {
            if (notesLayout.childCount == 0) {
                showToast("Add at least one note before starting the timer.")
                return
            }

            countDownTimer = object : CountDownTimer(timeLeftInMillis, countdownInterval) {
                override fun onTick(millisUntilFinished: Long) {
                    timeLeftInMillis = millisUntilFinished
                    updateTimer()
                    updateStatusText()
                }

                override fun onFinish() {
                    isTimerRunning = false
                    startButton.isEnabled = true
                    pauseButton.isEnabled = false
                    resetButton.isEnabled = true

                    if (timeLeftInMillis <= 0) {
                        resetTimer()
                        playSound()
                    }
                }
            }.start()

            isTimerRunning = true
            startButton.isEnabled = false
            pauseButton.isEnabled = true
            resetButton.isEnabled = false
            pomodoroButton.isEnabled = false
            breakButton.isEnabled = false
        }
    }

    private fun playSound() {
        mediaPlayer.start()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setPomodoroTimer() {
        if (!isTimerRunning) {
            isPomodoroTimer = true
            timeLeftInMillis = workDuration
            updateTimer()
        }
    }

    private fun setBreakTimer() {
        if (!isTimerRunning) {
            isPomodoroTimer = false
            timeLeftInMillis = breakDuration
            updateTimer()
        }
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
        isTimerRunning = false
        startButton.isEnabled = true
        pauseButton.isEnabled = false
        resetButton.isEnabled = true
        pomodoroButton.isEnabled = true
        breakButton.isEnabled = true
    }

    private fun resetTimer() {
        countDownTimer.cancel()
        isTimerRunning = false
        timeLeftInMillis = workDuration
        updateTimer()
        startButton.isEnabled = true
        pauseButton.isEnabled = false
        resetButton.isEnabled = false
        statusTextView.text = ""
        statusTextView.visibility = TextView.INVISIBLE
        pomodoroButton.isEnabled = true
        breakButton.isEnabled = true
    }

    private fun updateTimer() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        val timeLeftFormatted = String.format("%02d:%02d", minutes, seconds)
        timerTextView.text = timeLeftFormatted
    }

    private fun updateStatusText() {
        if (isPomodoroTimer) {
            statusTextView.text = "Do your task!"
            statusTextView.visibility = TextView.VISIBLE
        } else {
            statusTextView.text = "Take a break!"
            statusTextView.visibility = TextView.VISIBLE
        }
    }
}
