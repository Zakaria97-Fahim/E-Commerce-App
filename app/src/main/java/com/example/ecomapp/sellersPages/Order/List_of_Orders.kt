package com.example.ecomapp.to_sell.Order

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

class List_of_Orders : AppCompatActivity() {

    private lateinit var adapter: Adapter_card_order
    private lateinit var productList: ArrayList<Product>
    private lateinit var suggestionAdapter: Adapter_Searchs
    var userEmail :String = ""
    lateinit var db2:Products_DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.orders)

        val title = findViewById<TextView>(R.id.title)
        title.setText("Orders")
        val searchView: SearchView = findViewById(R.id.sea)
        try {
            val searchSrcTextViewField = SearchView::class.java.getDeclaredField("mSearchSrcTextView")
            searchSrcTextViewField.isAccessible = true
            val searchTextView = searchSrcTextViewField.get(searchView) as TextView
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
        userEmail = sharedPref.getString("email", null) ?: ""

        // Load products for the user's panier products
        loadAddedProducts(db2, userEmail)

        // Setup search functionality
        setupSearchView(searchView)
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

        // Retrieve IDs of products added by the user
        // Retrieve all products from the database
        val products: List<Product> = db2.getAll().filter { it.Email == userEmail }

        // Add products to the cart based on IDs
        for (product in products) {
            productList.add(product)
        }
        val sortedProducts = products.sortedWith(compareBy<Product> {
            when (it.etats) {
                "Confirmed" -> 0
                "Wait Confirmation" -> 1
                "no sell" -> 2
                else -> 3 // Any other value not covered above
            }
        })


        // Set up RecyclerView adapter
        adapter = Adapter_card_order(this, sortedProducts.toMutableList())
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


    private fun displayFilteredProducts(filteredProducts: List<Product>) {
        val list: RecyclerView = findViewById(R.id.listpannier)
        productList.clear()
        productList.addAll(filteredProducts)
        val adapter = Home_Adapter_card(this, productList)
        list.adapter = adapter
        adapter.notifyDataSetChanged()
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
