package com.example.ecomapp.to_sell.View_products

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.R
import com.example.ecomapp.customer_home.Home
import com.example.ecomapp.customer_home.Home_Adapter_card
import com.example.ecomapp.database.Product
import com.example.ecomapp.database.Products_DB
import com.example.ecomapp.home_pour_visiteur.Home_pour_Visiteur
import com.example.ecomapp.inscrip.UserSessionManager
import com.example.ecomapp.toBuy.Adapter_Searchs
import com.example.ecomapp.to_sell.Selling

class Your_Products : AppCompatActivity() {

    private lateinit var adapter: Adapter_Seller_Products
    private lateinit var productList: ArrayList<Product>
    private lateinit var suggestionAdapter: Adapter_Searchs
    var userEmail :String = ""
    lateinit var db2:Products_DB
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.your_products)
        val title = findViewById<TextView>(R.id.title)
        title.setText("Your Products")
        val search: SearchView = findViewById(R.id.sea)
        try {
            val searchSrcTextViewField = SearchView::class.java.getDeclaredField("mSearchSrcTextView")
            searchSrcTextViewField.isAccessible = true
            val searchTextView = searchSrcTextViewField.get(search) as TextView
            searchTextView.setTextColor(getColor(R.color.black))
            searchTextView.setHintTextColor(getColor(R.color.grey)) // If you want to change the hint color as well
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        val backButton: ImageButton = findViewById(R.id.back)
        backButton.setOnClickListener { onBackPressed() }
        setupMoreButton()
        db2 = Products_DB(applicationContext)

        // Retrieve user email from SharedPreferences
        val sharedPref = applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        userEmail = sharedPref.getString("email", null).toString()
        // Load products for the seller products
        loadAddedProducts(db2, userEmail)
        // Setup search functionality
        setupSearchView(search)
    }
    private fun setupMoreButton() {
        val moreButton: ImageButton = findViewById(R.id.settingsP)

        moreButton.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }
    private fun loadAddedProducts(db2: Products_DB, userEmail: String) {
        val list: RecyclerView = findViewById(R.id.listpannier)
        list.layoutManager = LinearLayoutManager(this)
        productList = ArrayList()

        // Retrieve all products from the database
        val products: ArrayList<Product> = db2.getAll()
        for (product in products) {
            if(product.Email==userEmail){
                productList.add(product)
            }
        }
        // Set up RecyclerView adapter
        adapter = Adapter_Seller_Products(userEmail,this, productList)
        list.adapter = adapter
        adapter.notifyDataSetChanged()
    }


    private fun displayFilteredProducts(filteredProducts: List<Product>) {
        val list: RecyclerView = findViewById(R.id.listpannier)
        productList.clear()
        productList.addAll(filteredProducts)
        val adapter = Home_Adapter_card(this, productList)
        list.adapter = adapter
        adapter.notifyDataSetChanged()
    }
    private fun setupSearchView(searchView: SearchView) {
        val suggestionList: ListView = findViewById(R.id.seaList)
        suggestionList.visibility = View.GONE

        val suggestions = productList.map { it.Name }.toMutableList()
        suggestionAdapter = Adapter_Searchs(this, suggestions)
        suggestionList.adapter = suggestionAdapter

        // Handle search query submission
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchText ->
                    val filteredProducts = productList.filter { it.Name.contains(searchText, ignoreCase = true) }
                    displayFilteredProducts(filteredProducts)
                }
                suggestionList.visibility = View.GONE
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    suggestionList.visibility = View.GONE
                    return true
                }
                suggestionList.visibility = View.VISIBLE
                newText?.let { text ->
                    val filteredSuggestions = productList.filter { it.Name.contains(text, ignoreCase = true) }
                    suggestionAdapter.clear()
                    suggestionAdapter.addAll(filteredSuggestions.map { it.Name })
                    suggestionAdapter.notifyDataSetChanged()
                    //displayFilteredProducts(filteredSuggestions)
                }
                return true
            }
        })

        // Hide suggestion list when SearchView is closed
        searchView.setOnCloseListener {
            suggestionList.visibility = View.GONE
            searchView.clearFocus()
            loadAddedProducts(db2, userEmail)

            false
        }

        // Handle suggestion item click
        suggestionList.setOnItemClickListener { _, _, position, _ ->
            val clickedSuggestion = suggestionAdapter.getItem(position) as String
            searchView.setQuery(clickedSuggestion, true)
            val searchProducts = productList.filter { it.Name == clickedSuggestion }
            displayFilteredProducts(searchProducts)
        }
    }


    //Settings===================================================================================
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
                    val i = Intent(this, Home_pour_Visiteur::class.java)
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
