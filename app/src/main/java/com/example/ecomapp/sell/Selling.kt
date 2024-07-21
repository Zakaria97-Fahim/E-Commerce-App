package com.example.ecomapp.sell

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.ecomapp.R
import com.example.ecomapp.VisitorHome
import com.example.ecomapp.sell.Order.Orders
import com.example.ecomapp.sell.AddProduct.AddProduct
import com.example.ecomapp.sell.View_products.SellerProducts

class Selling : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selling_pages)

        val btnAddPro = findViewById<Button>(R.id.add)
        val btnViewPr = findViewById<Button>(R.id.view)
        val btnOrders = findViewById<Button>(R.id.orders)
        val btnLogout = findViewById<Button>(R.id.logout)

        btnAddPro.setOnClickListener {
            val i = Intent(this, AddProduct :: class.java)
            startActivity(i)
        }

        btnViewPr.setOnClickListener {
            val i = Intent(this, SellerProducts :: class.java)
            startActivity(i)
        }

        btnOrders.setOnClickListener {
            val i = Intent(this, Orders :: class.java)
            startActivity(i)
        }

        btnLogout.setOnClickListener {
            val i = Intent(this, VisitorHome :: class.java)
            startActivity(i)
        }
    }
}