package com.example.ecomapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.database.Product
import com.example.ecomapp.database.Products_DB
import com.example.ecomapp.inscrip.LogIn
import com.example.ecomapp.inscrip.SignUp
import com.example.ecomapp.visitorHome.VisitorAdapter

class VisitorHome : AppCompatActivity() {

    private lateinit var productList    : ArrayList<Product>
    private lateinit var searchView     : SearchView
    private lateinit var searchListView : ListView
    private lateinit var listRecycler   : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        productList = ArrayList()

        // Recieve Widgets
        searchView     = findViewById(R.id.searchPrds)
        searchListView = findViewById(R.id.searchList)
        listRecycler   = findViewById(R.id.listPrs)

        // SearchView
        val s = Search(this, searchView, searchListView, productList, ::fillRecycleView)
        s.styleSView(searchView)
        s.setupSearchView()
        // Menu of Settings
        settingsButton()
        // Shopping Cart
        cartButton()
        // Filter Button
        filterButton()
        // Category Men, Women and Kids
        categoryAtSpinner()

        // get All Product from DataBase
        val db      = Products_DB(applicationContext)
        productList = db.getAllProducts()

        // get the Filters
        val filter = intent.getStringExtra("fil").toString()

        if (filter == "Filter"){
            // get the Product after filter and display it
            productList = listOfProducts()
        }else{
            // display all Products
            fillRecycleView(productList)
        }
    }

    //Display Menu of Settings
    private fun settingsButton() {
        val moreButton: ImageButton = findViewById(R.id.settings)
        moreButton.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }
    // GO to ProductCart
    private fun cartButton() {
        val panier: ImageButton = findViewById(R.id.shoppingCart)
        panier.setOnClickListener {
            val i = Intent(this, LogIn::class.java)
            startActivity(i)
        }
    }
    // FilterButton
    private fun filterButton() {
        val filter: Button = findViewById(R.id.filter)
        filter.setOnClickListener {
            val i = Intent(this, FilterActivity::class.java)
            startActivity(i)
        }
    }
    // Filterted List
    private fun listOfProducts():ArrayList<Product> {
        // get the filter elements
        val minPrice = intent.getIntExtra("minPrice", 0)
        val maxPrice = intent.getIntExtra("maxPrice", 5000)
        val discount = intent.getIntExtra("discount", 0)
        val review   = intent.getDoubleExtra("review", 0.0)
        // get the filtring Product
        val listFiltered = productList.filter {
            it.Price in minPrice.toDouble()..maxPrice.toDouble()
                    && it.remise >= discount
                    && it.review >= review
        }
        val filteredProducts = ArrayList<Product>()
        filteredProducts.addAll(listFiltered)
        // display those Products
        fillRecycleView(filteredProducts)
        return filteredProducts
    }

    // Category Spinner()
    private fun categoryAtSpinner() {
        val category: Spinner = findViewById(R.id.category)

        val choices = listOf("Select Category", "Men", "Women", "Kids")
        // fill the Spinner
        val spinnerAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, choices )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        category.adapter = spinnerAdapter

        category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = parent?.getItemAtPosition(position).toString()

                //List Of Produts By Category
                val  listFP: ArrayList<Product> = arrayListOf()

                if (selectedCategory == "Select Category") {
                    //get all filtered Products
                    listFP.addAll(productList)
                } else {
                    //get only Men or Women or Kids Products
                    listFP.addAll(productList.filter { it.Category == selectedCategory })
                }
                fillRecycleView(listFP)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    // Filling RecycleView
    fun fillRecycleView(products: ArrayList<Product>) {
        val adapter = VisitorAdapter(this, products)
        listRecycler.layoutManager = LinearLayoutManager(this)
        listRecycler.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    // Menu
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.main_menu1, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    val i = Intent(this, this::class.java)
                    startActivity(i)
                    true
                }
                R.id.menu_account -> {
                    val i = Intent(this, LogIn::class.java)
                    startActivity(i)
                    true
                }
                R.id.menu_sell -> {
                    val session = UserSessionManager(applicationContext)
                    session.logoutUser()
                    val i = Intent(this, LogIn::class.java)
                    startActivity(i)
                    true
                }
                R.id.menu_signup -> {
                    val i = Intent(this, SignUp::class.java)
                    startActivity(i)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

}
