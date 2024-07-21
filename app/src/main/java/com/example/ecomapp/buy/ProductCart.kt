package com.example.ecomapp.buy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.R
import com.example.ecomapp.Search
import com.example.ecomapp.customerHome.Home
import com.example.ecomapp.database.ProductsCart_DB
import com.example.ecomapp.database.ProductsCart
import com.example.ecomapp.database.Product
import com.example.ecomapp.database.Products_DB
import com.example.ecomapp.UserSessionManager
import com.example.ecomapp.VisitorHome
import com.example.ecomapp.sell.Selling

class ProductCart : AppCompatActivity() {

    private lateinit var adapter     : AdapterCart
    private lateinit var productList : ArrayList<Product>
    private lateinit var cart        : List<ProductsCart>
    private lateinit var clientEmail : String
    private lateinit var dbProducts  : Products_DB
    private lateinit var dbCarts     : ProductsCart_DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.carts)

        productList = ArrayList()
        // Back to last page
        val backButton: ImageButton = findViewById(R.id.back)
        backButton.setOnClickListener { onBackPressed() }
        // Settings
        setupMoreButton()
        // get all products cart
        dbCarts    = ProductsCart_DB(applicationContext)
        // get all products
        dbProducts = Products_DB(applicationContext)
        // Retrieve product ID and user email from Intent extras
        val productId   = intent.getIntExtra("productId", 0)
        val sellerEmail = intent.getStringExtra("sellerEmail").toString()
        // Retrieve user email from SharedPreferences
        val sharedPref = applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        clientEmail = sharedPref.getString("email", null) ?: ""
        //add new product to Panier database
        val isProductExist = dbCarts.checkIfProductExists(clientEmail,productId)
        if (productId != 0 && clientEmail != sellerEmail && !isProductExist){
            val pannierProduct = ProductsCart(sellerEmail, productId, clientEmail)
            dbCarts.addToCart(pannierProduct)
        }
        // Load products for the user's Cart
        loadCartProducts()
        // search functionality
        val searchView: SearchView = findViewById(R.id.sea)
        val searchListView: ListView = findViewById(R.id.seaList)
        val s = Search(this, searchView, searchListView, productList, ::fillRecycleView)
        s.styleSView(searchView)
        s.setupSearchView()
    }
    // *
    private fun loadCartProducts() {
        val all  = dbProducts.getAllProducts()
        cart = dbCarts.getAllCartProducts().filter { it.userEmail == clientEmail }
        for (p in cart){
            val product = all.find{ it.Id == p.id }
            if (product != null) {
                productList.add(product)
            }
        }
        // Set up RecyclerView adapter
        fillRecycleView(productList)
    }
    // Filling RecycleView
    private fun fillRecycleView(products: ArrayList<Product>) {
        val list: RecyclerView = findViewById(R.id.listpannier)
        list.layoutManager = LinearLayoutManager(this)
        adapter = AdapterCart(this, products)
        list.adapter = adapter
        adapter.notifyDataSetChanged()
    }

//Settings =========================================================================================
    private fun setupMoreButton() {
        val moreButton: ImageButton = findViewById(R.id.settingsP)
        moreButton.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.main_menu2, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    val i = Intent(this, Home::class.java)
                    startActivity(i)
                    true
                }
                R.id.menu_account -> {
                    val i = Intent(this, ProductCart::class.java)
                    startActivity(i)
                    true
                }
                R.id.menu_logout -> {
                    val session = UserSessionManager(applicationContext)
                    session.logoutUser()
                    val i = Intent(this, VisitorHome::class.java)
                    startActivity(i)
                    true
                }
                R.id.menu_sell -> {
                    val i = Intent(this, Selling::class.java)
                    startActivity(i)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}
