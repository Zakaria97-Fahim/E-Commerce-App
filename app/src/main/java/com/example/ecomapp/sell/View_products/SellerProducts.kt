package com.example.ecomapp.sell.View_products

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
import com.example.ecomapp.customerHome.Home
import com.example.ecomapp.database.Product
import com.example.ecomapp.database.Products_DB
import com.example.ecomapp.UserSessionManager
import com.example.ecomapp.Adapter_Searchs
import com.example.ecomapp.Search
import com.example.ecomapp.VisitorHome
import com.example.ecomapp.sell.Selling

class SellerProducts : AppCompatActivity() {

    private lateinit var adapter: AdapterSellerProducts
    private lateinit var productList: ArrayList<Product>
    private lateinit var suggestionAdapter: Adapter_Searchs

    var userEmail    :String = ""

    lateinit var db2 :Products_DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.your_products)

        productList = ArrayList()

        // Back to last open page
        val backButton: ImageButton = findViewById(R.id.back)
        backButton.setOnClickListener { onBackPressed() }

        // Settings
        setupMoreButton()

        // get all Products
        db2 = Products_DB(applicationContext)

        // Retrieve user email from SharedPreferences
        val sharedPref = applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        userEmail = sharedPref.getString("email", null).toString()

        // Load the seller products
        loadAddedProducts()
        // search functionality
        val searchView: SearchView = findViewById(R.id.sea)
        val searchListView: ListView = findViewById(R.id.seaList)
        val s = Search(this, searchView, searchListView, productList, ::fillRecycleView)
        s.styleSView(searchView)
        s.setupSearchView()
    }
    // Settings()
    private fun setupMoreButton() {
        val moreButton: ImageButton = findViewById(R.id.settingsP)
        moreButton.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }
    // load Seller Products
    private fun loadAddedProducts() {
        // Retrieve all products from the database
        val products = db2.getAllProducts().filter { it.sellerEmail == userEmail }
        productList.addAll(products)
        // Set up RecyclerView adapter
        fillRecycleView(productList)
    }
    // Filling RecycleView
    private fun fillRecycleView(products: ArrayList<Product>) {
        val list: RecyclerView = findViewById(R.id.listpannier)
        list.layoutManager = LinearLayoutManager(this)
        adapter = AdapterSellerProducts(userEmail,this, products)
        list.adapter = adapter
        adapter.notifyDataSetChanged()
    }


    //Settings()===================================================================================
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
                    val i = Intent(this, Selling::class.java)
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
