package com.example.ecomapp.sell.Order

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
import com.example.ecomapp.Search
import com.example.ecomapp.VisitorHome
import com.example.ecomapp.sell.Selling

class Orders : AppCompatActivity() {

    // Adapter
    private lateinit var adapter: OrderAdapter
    // all seller orders products
    private lateinit var productList: ArrayList<Product>
    //email of the user
    lateinit private var sellerEmail :String
    // var to get database of all products
    lateinit private var db:Products_DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.orders)
        productList = ArrayList()


        // comeback to the last open page
        val backButton: ImageButton = findViewById(R.id.back)
        backButton.setOnClickListener { onBackPressed() }
        // Seattings (repeated)
        SettingButton()
        // Get All Products
        db = Products_DB(applicationContext)
        // Retrieve seller email from SharedPreferences
        val sharedPref = applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        sellerEmail    = sharedPref.getString("email", null) ?: ""
        // Load products for the Seller products
        loadSelledProducts()

        // search functionality
        val searchView: SearchView = findViewById(R.id.sea)
        val searchListView: ListView = findViewById(R.id.seaList)
        val s = Search(this, searchView, searchListView, productList, ::fillRecycleView)
        s.styleSView(searchView)
        s.setupSearchView()
    }

    // Seattings
    private fun SettingButton() {
        val moreButton: ImageButton = findViewById(R.id.settingsP)
        moreButton.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }

    // Load Selled Products
    private fun loadSelledProducts() {
        // Retrieve all products from the database
        val products: List<Product> = db.getAllProducts().filter { it.sellerEmail == sellerEmail }
        // add its at productList to display at RecycleView
        productList.addAll(products)
        fillRecycleView(productList)
    }

    // Filling RecycleView
    private fun fillRecycleView(products: ArrayList<Product>) {
        val list: RecyclerView = findViewById(R.id.listpannier)
        list.layoutManager = LinearLayoutManager(this)
        adapter = OrderAdapter(this, products)
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
