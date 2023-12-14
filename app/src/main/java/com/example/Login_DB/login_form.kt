package com.example.Login_DB

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.Login_DB.databinding.ActivityLoginFormBinding

class login_form : AppCompatActivity() {
    private lateinit var bind: ActivityLoginFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityLoginFormBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val dbhelp = DB_class(applicationContext)
        val db = dbhelp.readableDatabase

        bind.btnlogin.setOnClickListener {
            val username = bind.logtxt.text.toString()
            val password = bind.ed3.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                val query = "SELECT * FROM user WHERE username=? AND password=?"
                val selectionArgs = arrayOf(username, password)
                val rs = db.rawQuery(query, selectionArgs)

                if (rs.moveToFirst()) {
                    val name = rs.getString(rs.getColumnIndex("name"))
                    val lastname = rs.getString(rs.getColumnIndex("lastname"))
                    val email = rs.getString(rs.getColumnIndex("email"))

                    //change here
                    val intent = Intent(this, Pomodoro_timer::class.java)
                    intent.putExtra("uname", name)  // Use "uname" instead of "username"
                    intent.putExtra("email", email)
                    intent.putExtra("lastname", lastname)
                    startActivity(intent)
                } else {
                    showAlertDialog("Message", "Username or password is incorrect/blank!")
                }
            } else {
                showAlertDialog("Message", "Username or password is incorrect/blank!")
            }
        }

        bind.regisLink.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showAlertDialog(title: String, message: String) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(title)
        alertDialog.setMessage(message)
        alertDialog.setPositiveButton("Ok", null)
        alertDialog.show()
    }
}
