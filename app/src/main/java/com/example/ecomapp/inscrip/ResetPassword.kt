package com.example.ecomapp.inscrip

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecomapp.R
import com.example.ecomapp.database.User_DB

class ResetPassword : AppCompatActivity() {

    private lateinit var userDatabase: User_DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reset_password)

        userDatabase  = User_DB(this)
        // get user Email from CodeConfirmation page to Update the Password
        val userEmail = intent.getStringExtra("userMail")

        val newPassword     : EditText = findViewById(R.id.passw)
        val confirmPassword : EditText = findViewById(R.id.confirm2)
        val resetButton     : Button   = findViewById(R.id.reset)

        resetButton.setOnClickListener {
            if (newPassword != confirmPassword ) {
                Toast.makeText(applicationContext, "Password Mismatch !!!", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }else{
                if (userEmail != null) {
                    // Update the Password at user database
                    userDatabase.updatePassword(userEmail,newPassword.toString())
                    Toast.makeText(applicationContext, "saved successfully !!!", Toast.LENGTH_LONG).show()
                    // open Log in page
                    val i = Intent(this, LogIn::class.java)
                    i.putExtra("userMail", userEmail)
                    startActivity(i)
                }
            }
        }
    }
}
