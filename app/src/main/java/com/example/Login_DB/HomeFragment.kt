import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.Login_DB.R

class HomeFragment : Fragment(), Pomodoro_timer.PomodoroCallback {

    private lateinit var timerTextView: TextView
    private lateinit var startButton: Button
    private lateinit var pauseButton: Button
    private lateinit var resetButton: Button
    private lateinit var pomodoroButton: Button
    private lateinit var breakButton: Button
    private lateinit var addNotesButton: Button
    private lateinit var notesLayout: LinearLayout
    private lateinit var pomodoroTimer: Pomodoro_timer
    private var isTaskAdded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.timer_pomodoro, container, false)
        initializeViews(view)
        setClickListeners()
        return view
    }

    override fun onTimerTick(timeLeftInMillis: Long) {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        val timeLeftFormatted = String.format("%02d:%02d", minutes, seconds)
        timerTextView.text = timeLeftFormatted
    }

    override fun onTimerFinish() {
        pomodoroTimer.playSound()
    }

    private fun initializeViews(view: View) {
        timerTextView = view.findViewById(R.id.timerTextView)
        startButton = view.findViewById(R.id.startButton)
        pauseButton = view.findViewById(R.id.pauseButton)
        resetButton = view.findViewById(R.id.resetButton)
        pomodoroButton = view.findViewById(R.id.pomodoroButton)
        breakButton = view.findViewById(R.id.breakButton)
        addNotesButton = view.findViewById(R.id.addNotesButton)
        notesLayout = view.findViewById(R.id.notesLayout)

        pomodoroTimer = Pomodoro_timer(this, notesLayout)
        pomodoroTimer.setPomodoroTimer()
    }

    private fun setClickListeners() {
        startButton.setOnClickListener {
            if (isTaskAdded) {
                if (notesLayout.childCount == 0) {
                    showToast("Add at least one note before starting the timer.")
                } else {
                    pomodoroTimer.startPomodoro()
                    // Enable pause and reset buttons when the timer is started
                    // No need to disable other buttons
                }
            } else {
                showToast("Add a task before starting Pomodoro!")
            }
        }

        // Initially block the pause and reset buttons


        pauseButton.setOnClickListener {
            // Check if the timer is running before allowing pause
            if (pomodoroTimer.isTimerRunning()) {
                pomodoroTimer.pauseTimer()
            } else {
                showToast("Start the timer first.")
            }
        }

        resetButton.setOnClickListener {
            startButton.isEnabled = true
            pomodoroTimer.resetTimer()
        }
        pomodoroButton.setOnClickListener {
            pomodoroTimer.setPomodoroTimer()
        }

        breakButton.setOnClickListener {
            pomodoroTimer.setBreakTimer()
        }


        addNotesButton.setOnClickListener { showAddNoteDialog() }
    }

    private fun showAddNoteDialog() {
        val dialog = CustomDialog(requireContext(), object : CustomDialog.OnAddNoteListener {
            override fun onAddNoteClicked(taskDetails: String) {
                pomodoroTimer.addTaskDetails(taskDetails)
                isTaskAdded = true // Set isTaskAdded to true when a task is added
                // Enable the start button when a task is added
                startButton.isEnabled = true
            }
        })
        dialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}
