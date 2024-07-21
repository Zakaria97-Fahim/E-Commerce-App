package com.example.ecomapp.inscrip

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.ecomapp.customerHome.Home
import com.example.ecomapp.R
import com.example.ecomapp.database.User
import com.example.ecomapp.database.User_DB

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)

        val btnAdd = findViewById<Button>(R.id.sign)
        val login  = findViewById<TextView>(R.id.to_log_in)
        val db = User_DB(applicationContext)

        //Already Have Account
        login.setOnClickListener {
            val i = Intent(this, LogIn :: class.java)
            startActivity(i)
        }

        //Add a new User
        btnAdd.setOnClickListener {
            val nom         = findViewById<EditText>(R.id.nom).text.toString()
            val prenom      = findViewById<EditText>(R.id.prenom).text.toString()
            val email       = findViewById<EditText>(R.id.email).text.toString()
            val password    = findViewById<EditText>(R.id.password).text.toString()
            val confirmpass = findViewById<EditText>(R.id.confipass).text.toString()

            /* check if the Email-user exists */
            if (db.checkIfEmailExists(email)) {
                Toast.makeText(
                    applicationContext,
                    "Email already exists. Please choose another one.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            /* check if the Password identical */
            else if (password != confirmpass ) {
                Toast.makeText(applicationContext, "Password mismatch !!!", Toast.LENGTH_LONG)
                    .show()
            }
            /* Add the User */
            else {
                //create user object
                val userx = User(nom, prenom, email, password)
                //add user to database User
                val addUser = db.addUser(userx)

                // when user added, remeber the user login
                if (addUser != (-1).toLong()) {
                    Toast.makeText(applicationContext, "Succesfuly Added", Toast.LENGTH_LONG).show()

                    // remembre the user login
                    val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putString("email", email)
                    editor.apply()

                    // went to Home
                    val i = Intent(this, Home :: class.java)
                    startActivity(i)

                } else {
                    Toast.makeText(applicationContext, "Failure", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
