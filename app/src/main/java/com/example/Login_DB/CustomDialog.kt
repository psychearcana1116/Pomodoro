import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.Login_DB.R

class CustomDialog(context: Context, var onAddNoteListener: OnAddNoteListener) : Dialog(context) {

    private lateinit var etTaskDetails: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_dialog)

        etTaskDetails = findViewById(R.id.etTaskDetails)
        val btnOK: Button = findViewById(R.id.btnOK)
        val btnCancel: Button = findViewById(R.id.btnCancel)

        btnOK.setOnClickListener {
            val taskDetails = etTaskDetails.text.toString().trim()
            if (taskDetails.isNotEmpty()) {
                onAddNoteListener.onAddNoteClicked(taskDetails)
                dismiss()
            } else {
                Toast.makeText(context, "Please enter task details", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel.setOnClickListener {
            dismiss()
        }
    }

    interface OnAddNoteListener {
        fun onAddNoteClicked(taskDetails: String)
    }
}
