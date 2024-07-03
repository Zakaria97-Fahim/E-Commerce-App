package com.example.ecomapp.customer_home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.SearchView
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.R
import com.example.ecomapp.home_pour_visiteur.Home_pour_Visiteur
import com.example.ecomapp.database.Product
import com.example.ecomapp.database.Products_DB
import com.example.ecomapp.inscrip.UserSessionManager
import com.example.ecomapp.toBuy.Adapter_Searchs
import com.example.ecomapp.toBuy.Panier
import com.example.ecomapp.to_sell.Selling

class Home : AppCompatActivity() {
//    private lateinit var suggestionAdapter: Adapter_Searchs
    private lateinit var productList: ArrayList<Product>
    private lateinit var search: SearchView
    private lateinit var suggestionList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        val list: RecyclerView = findViewById(R.id.listPrs)

        search = findViewById(R.id.searchPrds)

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

        suggestionList = findViewById(R.id.searchList)
        val db = Products_DB(applicationContext)
        val allProducts = db.getAll()
        productList = ArrayList()
        for (p in allProducts){
            if (p.QRest >0 ){
                productList.add(p)
            }
        }

        var filter :String
        setupMoreButton()
        setupPanierButton()
        setupFilterButton()

        filter = intent.getStringExtra("fil").toString()

        if (filter.equals("Filter")){
            productList = listOfProducts()
        }else{
            callAdapter(productList, list)
        }
        setupCategorySpinner(productList)
        setupSearchView(search)

    }
    //Search=====================================================================================
    private fun displayFilteredProducts(filteredProducts: List<Product>) {
      val list: RecyclerView = findViewById(R.id.listPrs)
      productList.clear()
      productList.addAll(filteredProducts)
      val adapter = Home_Adapter_card(this, productList)
      list.adapter = adapter
      adapter.notifyDataSetChanged()
    }
    private fun setupSearchView(searchView: SearchView) {
        suggestionList.visibility = View.GONE

        val suggestions = productList.map { it.Name }.toMutableList()
        val suggestionAdapter = Adapter_Searchs(this, suggestions)
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
            val db = Products_DB(applicationContext)
            val allProducts = db.getAll()
            productList.clear()
            for (p in allProducts){
                if (p.QRest > 0 ){
                    productList.add(p)
                }
            }
            updateProductList(productList)
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



    //Display Menu of Settings========================================================================
    private fun setupMoreButton() {
        val moreButton: ImageButton = findViewById(R.id.settings)

        moreButton.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }
   //GO to Panier===================================================================================
    private fun setupPanierButton() {
        val panier = findViewById<ImageButton>(R.id.shopcard)
        panier.setOnClickListener {
            val i = Intent(this, Panier::class.java)
            startActivity(i)
        }
    }
   //FilterButton===================================================================================
    private fun setupFilterButton() {
        val filterP: Button = findViewById(R.id.filter)
        filterP.setOnClickListener {
            val i = Intent(this, FilterActivity::class.java)
            startActivity(i)
        }
    }

   //diplay Products===================================================================================

    private fun callAdapter(products: List<Product>, list: RecyclerView) {
        val adapter = Home_Adapter_card(this, products.toMutableList())
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter
    }
    private fun updateProductList(ListP : ArrayList<Product>) {
        val list: RecyclerView = findViewById(R.id.listPrs)
        callAdapter(ListP, list)
    }

   //Filterted List ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++=
    private fun listOfProducts():ArrayList<Product> {
        // Check if the intent contains filter parameters
        val minPrice = intent.getIntExtra("minPrice", 0)
        val maxPrice = intent.getIntExtra("maxPrice", 50000)
        val discount = intent.getIntExtra("discount", 0)
        val review = intent.getDoubleExtra("review", 0.0)
        val filter_Pro = ArrayList<Product>()
        for (product in productList) {
            val priceInRange = product.Price in minPrice.toDouble()..maxPrice.toDouble()
            val hasDiscount = product.remise >= discount
            val hasReview = product.review >= review
            if (priceInRange && hasDiscount && hasReview) {
                filter_Pro.add(product)
            }
        }
        updateProductList(filter_Pro)
        return filter_Pro
    }

   //Category Spinner===============================================================================
    private fun setupCategorySpinner(filter_Pro:ArrayList<Product>) {
        val category: Spinner = findViewById(R.id.category)
        val choixCtg = listOf("Select Category", "Men", "Women", "Kids")
        val spinnerAdapter = ArrayAdapter(
            this,
            R.layout.spinner_dropdown_item,
            choixCtg
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        category.adapter = spinnerAdapter

        category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                filterProductsByCategory(selectedItem,filter_Pro)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
    private fun filterProductsByCategory(category: String, filter_Pro:ArrayList<Product>) {
        val  ListP: ArrayList<Product> = arrayListOf()
        if (category == "Select Category") {
            ListP.addAll(filter_Pro)
        } else {
            ListP.addAll(filter_Pro.filter { it.Category == category })
        }
        updateProductList(ListP)
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
                    val i = Intent(this, Panier::class.java)
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
