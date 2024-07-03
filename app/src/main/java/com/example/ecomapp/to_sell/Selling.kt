package com.example.ecomapp.to_sell

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.ecomapp.home_pour_visiteur.Home_pour_Visiteur
import com.example.ecomapp.R
import com.example.ecomapp.to_sell.Order.List_of_Orders
import com.example.ecomapp.to_sell.Add_product.Add_Product
import com.example.ecomapp.to_sell.View_products.Your_Products

class Selling : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selling_pages)

        val btn_Add = findViewById<Button>(R.id.add)
        val btnView = findViewById<Button>(R.id.view)
        val btnOrders = findViewById<Button>(R.id.orders)
        val btnLogout = findViewById<Button>(R.id.logout)

        btn_Add.setOnClickListener {
            val i = Intent(this, Add_Product :: class.java)
            startActivity(i)
        }

        btnView.setOnClickListener {
            val i = Intent(this, Your_Products :: class.java)
            startActivity(i)
        }

        btnOrders.setOnClickListener {
            val i = Intent(this, List_of_Orders :: class.java)
            startActivity(i)
        }

        btnLogout.setOnClickListener {
            val i = Intent(this, Home_pour_Visiteur :: class.java)
            startActivity(i)
        }

    }
}