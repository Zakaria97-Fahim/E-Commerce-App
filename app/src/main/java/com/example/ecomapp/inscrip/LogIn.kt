package com.example.ecomapp.inscrip

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.ecomapp.customerHome.Home
import com.example.ecomapp.R
import com.example.ecomapp.UserSessionManager
import com.example.ecomapp.database.User_DB

class LogIn : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.log_in)

        val login  = findViewById<Button>(R.id.login)
        val signup = findViewById<Button>(R.id.sign)
        //?
        val session = UserSessionManager(applicationContext)

// Log IN ==========================================================================================
        login.setOnClickListener {

            val email    = findViewById<EditText>(R.id.email).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()

            val db = User_DB(applicationContext)

            /* check if the Email and Password exists-------- */
            if ( db.checkIfEmailExists(email) ) {
                if ( db.checkIfPasswrdExists(email,password) ){
                    //?
                    session.setLogin(true)

                    val i = Intent(this, Home :: class.java)

                    //remember the login of the Customer
                    val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val editor     = sharedPref.edit()
                    editor.putString("email", email)
                    editor.apply()

                    //went to Home Customer page
                    startActivity(i)
                }else
                    Toast.makeText(applicationContext, "Password is Not Correct. Please try again.", Toast.LENGTH_SHORT).show()
            }
            else
                Toast.makeText(applicationContext, "Email does Not Exists. Please Sign UP.", Toast.LENGTH_SHORT).show()
        }

        // Transfer to Sing UP page-----------------------------------------------------------------
        signup.setOnClickListener {
            val ii = Intent(this, SignUp :: class.java)
            startActivity(ii)
        }

    }

}