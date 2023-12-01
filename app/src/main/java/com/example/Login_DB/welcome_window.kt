package com.example.Login_DB


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.Login_DB.databinding.ActivityWelcomeWindowBinding

class welcome_window : AppCompatActivity() {
    private lateinit var bind: ActivityWelcomeWindowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind= ActivityWelcomeWindowBinding.inflate(layoutInflater)
        setContentView(bind.root)

        // Retrieve username and email from the intent that started this activity
        val addUsername = intent.getStringExtra("uname")
        val addEmail = intent.getStringExtra("email")
        val addLastname = intent.getStringExtra("lastname")

        // Set the retrieved values to the respective TextViews
        bind.uname.text = "Username: $addUsername $addLastname"
        bind.email.text = "Email: $addEmail"

        bind.logout.setOnClickListener {
            startActivity(Intent(this, login_form::class.java))
        }
    }
}