package com.example.ecomapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import com.example.ecomapp.customerHome.Home
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //?
        val session = UserSessionManager(applicationContext)
        if (session.isLoggedIn()) {
            //went to Customer Home
            val i = Intent(this@MainActivity, Home::class.java)
            startActivity(i)
        } else {
            //wait 1.5 second then open the Visitor Home Page
            GlobalScope.launch(Dispatchers.Main) {
                delay(1500)
                //went to Visitor Home
                val i = Intent(this@MainActivity, VisitorHome::class.java)
                startActivity(i)
            }
        }
    }
}
