package com.example.Login_DB

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.Login_DB.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var dbhelp=DB_class(applicationContext)
        var db=dbhelp.writableDatabase

        binding.btnrgs.setOnClickListener {
            var name=binding.ed1.text.toString()
            var username=binding.ed2.text.toString()
            var password=binding.ed3.text.toString()
            val lastname = binding.ed4.text.toString()
            val email = binding.ed5.text.toString()

            if(name.isNotEmpty() && username.isNotEmpty() && isPasswordValid(password) && password.isNotEmpty()  && lastname.isNotEmpty() && email.isNotEmpty()) {
                var data = ContentValues()
                data.put("name", name)
                data.put("username", username)
                data.put("password", password)
                data.put("lastname", lastname)
                data.put("email", email)

                var rs:Long = db.insert("user", null, data)
                if(!rs.equals(-1)) {
                    var ad = AlertDialog.Builder(this)
                    ad.setTitle("Message")
                    ad.setMessage("Account registered successfully")
                    ad.setPositiveButton("Ok", null)
                    ad.show()
                    binding.ed1.text.clear()
                    binding.ed2.text.clear()
                    binding.ed3.text.clear()
                    binding.ed4.text.clear()
                    binding.ed5.text.clear()
                } else {
                    var ad = AlertDialog.Builder(this)
                    ad.setTitle("Message")
                    ad.setMessage("Record not added")
                    ad.setPositiveButton("Ok", null)
                    ad.show()
                    binding.ed1.text.clear()
                    binding.ed2.text.clear()
                    binding.ed3.text.clear()
                    binding.ed4.text.clear()
                    binding.ed5.text.clear()
                }
            } else if (password == null){
                showPasswordRequirementsDialog()
            } else if (!(isPasswordValid(password))){
                showPasswordRequirementsDialog()
            }
            else {
                showFailureDialog()
            }
        }
        binding.loginLink.setOnClickListener {
            val intent=Intent(this, login_form::class.java)
            startActivity(intent)
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        val minLength = 8
        val passwordPattern = "^(?=.*[0-9])(?=.*[@#\$%^&+=!])(?=.*[A-Z])(?=.*[a-z]).{$minLength,}$"

        return password.matches(Regex(passwordPattern))
    }

    private fun showPasswordRequirementsDialog() {
        val requirementsDialog = AlertDialog.Builder(this)
        requirementsDialog.setTitle("Password Requirements")
        requirementsDialog.setMessage("Password must be a combination of numbers, special characters, capital and small letters, and have a minimum length of 8 characters.")
        requirementsDialog.setPositiveButton("Ok", null)
        requirementsDialog.show()
    }

    private fun showFailureDialog() {
        val failureDialog = AlertDialog.Builder(this)
        failureDialog.setTitle("Message")
        failureDialog.setMessage("Record not added. Please complete the form to proceed")
        failureDialog.setPositiveButton("Ok", null)
        failureDialog.show()
    }
}