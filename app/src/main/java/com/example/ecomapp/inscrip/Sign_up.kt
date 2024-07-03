package com.example.ecomapp.inscrip

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.ecomapp.customer_home.Home
import com.example.ecomapp.R
import com.example.ecomapp.database.User
import com.example.ecomapp.database.User_DB

class Sign_up : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up)
        val btnAdd = findViewById<Button>(R.id.sign)
        val log_in = findViewById<TextView>(R.id.to_log_in)
//Already Have Account =============================================================================
        log_in.setOnClickListener {
            val i = Intent(this, Log_in :: class.java)
            startActivity(i)
        }
// Add a new User ==================================================================================
        btnAdd.setOnClickListener {

            val nom = findViewById<EditText>(R.id.nom).text.toString()
            val prenom = findViewById<EditText>(R.id.prenom).text.toString()
            val email = findViewById<EditText>(R.id.email).text.toString()
            val password = findViewById<EditText>(R.id.password).text.toString()
            val confirmpass = findViewById<EditText>(R.id.confipass).text.toString()

            val db = User_DB(applicationContext)


            /* check if the Email-user exists-------- */
            if (db.checkIfEmailExists(email))
                Toast.makeText(applicationContext, "Email already exists. Please choose another one.", Toast.LENGTH_SHORT).show()

            /*check if the Password-user exists-------*/
            else if (password != confirmpass && !db.checkIfEmailExists(email) )
                Toast.makeText(applicationContext, "Password pas identique", Toast.LENGTH_LONG).show()

            /* Add the User ------------------------- */
            else {
                val userx = User(nom, prenom, email, password)

                val resultat = db.ajouterUser(userx)

                if (resultat != (-1).toLong()) {
                    Toast.makeText(applicationContext, "Succesfuly Added", Toast.LENGTH_LONG).show()
                    val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putString("email", email)
                    editor.apply()
                    val i = Intent(this, Home :: class.java)
                    startActivity(i)
                } else {
                    Toast.makeText(applicationContext, "Failure", Toast.LENGTH_LONG).show()
                }
            }
        }

//==================================================================================================
    }
}
