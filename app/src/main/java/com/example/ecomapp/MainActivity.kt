package com.example.ecomapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import com.example.ecomapp.customer_home.Home
import com.example.ecomapp.home_pour_visiteur.Home_pour_Visiteur
import com.example.ecomapp.inscrip.UserSessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val session = UserSessionManager(applicationContext)
        if (session.isLoggedIn()) {
            val i = Intent(this@MainActivity, Home::class.java)
            startActivity(i)
        } else {
            GlobalScope.launch(Dispatchers.Main) {
                delay(1500)
                val i = Intent(this@MainActivity, Home_pour_Visiteur::class.java)
                startActivity(i)
            }
        }
    }
}
