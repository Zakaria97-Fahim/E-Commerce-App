package com.example.ecomapp

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ListView
import android.widget.SearchView
import android.widget.TextView
import com.example.ecomapp.database.Product
class Search(val context: Context,
             val searchView : SearchView,
             val searchListView: ListView,
             val productList: ArrayList<Product>,
             val fillRecycleView: (ArrayList<Product>) -> Unit
) {

    // Search
    fun setupSearchView() {
        searchListView.visibility = View.GONE

        val productsNames = productList.map { it.Name }.toMutableList()
        val searchAdapter = Adapter_Searchs(context, productsNames)
        searchListView.adapter = searchAdapter

        // Handle search query submission
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String): Boolean {
                //get Products by name searching
                val filteredProducts = productList.filter {
                    it.Name.contains(text, ignoreCase = true)
                }
                displaySearchingProducts(filteredProducts)

                searchListView.visibility = View.GONE

                // Hide the keyboard
                val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE)
                        as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(searchView.windowToken, 0)

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    searchListView.visibility = View.GONE
                    return true
                }

                searchListView.visibility = View.VISIBLE
                //get Products by newName searching
                val filteredProducts = productList.filter {
                    it.Name.contains(newText, ignoreCase = true)
                }
                displaySearchingProducts(filteredProducts)
                searchAdapter.clear()
                searchAdapter.addAll(filteredProducts.map { it.Name })
                searchAdapter.notifyDataSetChanged()

                return true
            }
        })

        // Hide search list when SearchView is closed
        searchView.setOnCloseListener {
            searchListView.visibility = View.GONE
            searchView.clearFocus()
            fillRecycleView(productList)
            false
        }

        // Handle search item click
        searchListView.setOnItemClickListener { _, _, position, _ ->
            val clickedName = searchAdapter.getItem(position) as String
            searchView.setQuery(clickedName, true)
            val searchProducts = productList.filter { it.Name == clickedName }
            displaySearchingProducts(searchProducts)
        }
    }
    private fun displaySearchingProducts(filteredProducts: List<Product>) {
        val searchProducts = ArrayList<Product>()
        searchProducts.addAll(filteredProducts)
        fillRecycleView(searchProducts)
    }
    // SearchView Style ----------------------------------------------------------------------------
    fun styleSView(searchView: SearchView){
        try {
            val searchSrcTextViewField = SearchView::class.java.getDeclaredField("mSearchSrcTextView")
            searchSrcTextViewField.isAccessible = true
            val searchTextView = searchSrcTextViewField.get(searchView) as TextView
            searchTextView.setTextColor(context.getColor(R.color.black))
            searchTextView.setHintTextColor(context.getColor(R.color.grey)) // If you want to change the hint color as well
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }
}