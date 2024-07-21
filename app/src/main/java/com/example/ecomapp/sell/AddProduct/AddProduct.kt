package com.example.ecomapp.sell.AddProduct

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.database.Product
import com.example.ecomapp.database.Products_DB
import com.example.ecomapp.R
import com.example.ecomapp.database.Image_DB
import com.example.ecomapp.database.Images
import com.example.ecomapp.sell.View_products.SellerProducts

class AddProduct : AppCompatActivity() {

    // Images website list
    private val urlList = ArrayList<String>()

    // var of Selecting Category Men or Women or Kids
    private var selectCategory: String = ""

    // Get DataBase of Products and Images
    lateinit var dbProducts : Products_DB
    lateinit var dbImages   : Image_DB

    //Widgets
    private lateinit var xName             : EditText
    private lateinit var xDescription      : EditText
    private lateinit var xBrand            : EditText
    private lateinit var xpriceText        : EditText
    private lateinit var xquantityText     : EditText
    private lateinit var xweightText       : EditText
    private lateinit var xdimensionText    : EditText
    private lateinit var xshippingCostText : EditText
    private lateinit var xstockText        : EditText
    private lateinit var xminThresholdText : EditText
    private lateinit var xremiseText       : EditText
    private lateinit var xCategory         : Spinner
    private lateinit var xaddImgButton     : Button
    private lateinit var xsaveButton       : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_product)

        var xhelper = 0

        // Initialize Widgets
        initWidgets()

        // get database of Products and images
        dbProducts = Products_DB(applicationContext)
        dbImages   = Image_DB(applicationContext)

        // get email of Seller using SharedPreferences
        val sharedPref = applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val sellerEmail = sharedPref.getString("email", null) ?: ""

        // Fill Spinner by Categorys
        categorys()
        // open The Fragment
        xaddImgButton.setOnClickListener {
            val fragment: Fragment = AddImagesFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container1, fragment)
                .addToBackStack(null)
                .commit()
        }
        // get the url from fragment and put it at urlList
        addUrl()

        /* Retrieve Product details to Editing it */

        // get the product ID to Update it
        val id = intent.getIntExtra("productId", 1000000)// change the 100000 by 0, and in database increase all id +1
        if (id != 1000000) {
            // Get All Products of the Seller
            val products = dbProducts.getAllSellerProducts(sellerEmail)
            // Get All Images of the Product
            val images = dbImages.getAllProductImages(id, sellerEmail)
            // get the selected Product
            val p = products.filter { it.Id == id }
            // set the details of the Product at Widgets
            setProductDetails(p[0])
            //
            if (p[0].Id == id) {
                // ?
                urlList.clear()
                // add images to a urlList
                urlList.addAll(images)
                // remove all empty url of the urlList
                urlList.removeAll { it.isBlank() }
                // display the Images
                adapter()
            }
            xhelper = 1
        }

        /* Add the Product to Products database */
        xsaveButton.setOnClickListener {
            // Get selected category
            val category = categorys()
            // Check if a Widget is Blank
            if (emptyorblank())
                return@setOnClickListener
            // Add the Product
            else if (xhelper == 0){
                // create an ID to the new Product
                val idp = generateId()
                // create Product Object
                val newProduct = productObject(sellerEmail, idp, category)
                // create Images Object
                val newImages = imagesObject(sellerEmail, idp)
                // add the Product to Products_DB
                dbProducts.addProduct(newProduct)
                // add the Images to Image_DB
                dbImages.addIMg(newImages)

                Toast.makeText(this, "Product $idp is saved", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, SellerProducts::class.java))
            }
            // Update the product
            else{
                // create Product Object
                val upProduct = productObject(sellerEmail, id, category)
                dbProducts.updateProduct(upProduct,id,sellerEmail)
                // create Images Object
                val upImages = imagesObject(sellerEmail, id)
                dbImages.updateIMg(upImages,id,sellerEmail)

                startActivity(Intent(this, SellerProducts::class.java))
            }
        }
    }

    // Set the Product Details to Update
    private fun setProductDetails(product: Product) {
        // Populate UI fields with product details
        xName.setText(product.Name)
        xDescription.setText(product.Description)
        xBrand.setText(product.Brand)
        spinnerCat(xCategory, product.Category)
        xpriceText.setText(product.Price.toString())
        xshippingCostText.setText(product.Sh_Cost.toString())
        xstockText.setText(product.Stock_Level.toString())
        xquantityText.setText(product.Quantity.toString())
        xminThresholdText.setText(product.Min_Threshold.toString())
        xweightText.setText(product.Weight.toString())
        xdimensionText.setText(product.dimension.toString())
        xremiseText.setText(product.remise.toString())
    }
    //  set Category When the user want to modify it, "part of Updaty product!"
    private fun spinnerCat(spinner: Spinner, category: String) {
        val categoriesList = listOf("Select category", "Men", "Women", "Kids")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoriesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        val position = categoriesList.indexOf(category)
        if (position != -1) {
            spinner.setSelection(position)
        } else {
            spinner.setSelection(0)
        }
    }


    // Fill the Spinner Category
    private fun categorys(): String {
        // List of Category
        val choices = listOf("Select Category", "Men", "Women", "Kids")
        // Spinner Text Color
        val textColor = resources.getColor(R.color.black)
        // Spinner Adapter
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            choices,
        )
        // the items look
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        xCategory.adapter = spinnerAdapter
        // When the Seller selected an Item
        xCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                selectCategory = when (position) {
                    0 -> "Select Category"
                    1 -> "Men"
                    2 -> "Women"
                    else -> "Kids"
                }
                (view as? TextView)?.setTextColor(textColor)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        return selectCategory
    }


    // Create Product object
    private fun productObject(email: String, id: Int, category: String): Product {
        val name         = xName.text.toString().let { if (it.isEmpty()) "" else it }
        val description  = xDescription.text.toString().let { if (it.isEmpty()) "" else it }
        val brand        = xBrand.text.toString().let { if (it.isEmpty()) "" else it }
        val price        = xpriceText.text.toString().toDoubleOrNull() ?: 0.0
        val quantity     = xquantityText.text.toString().toIntOrNull() ?: 0
        val weight       = xweightText.text.toString().toDoubleOrNull() ?: 0.0
        val dimension    = xdimensionText.text.toString().toDoubleOrNull() ?: 0.0
        val shippingCost = xshippingCostText.text.toString().toDoubleOrNull() ?: 0.0
        val stockLevel   = xstockText.text.toString().toIntOrNull() ?: 0
        val minThreshold = xminThresholdText.text.toString().toIntOrNull() ?: 0
        val remise       = xremiseText.text.toString().toIntOrNull() ?: 0

        return Product(
            email,
            id,
            name,
            description,
            category,
            brand,
            price,
            quantity,
            0,
            quantity,
            weight,
            dimension,
            shippingCost,
            stockLevel,
            minThreshold,
            remise,
            0.0,
            "No Sell"
        )
    }
    // Creat Image object
    private fun imagesObject(email: String, id: Int): Images {
        val i1  = urlList.getOrElse(0) { "" }
        val i2  = urlList.getOrElse(1) { "" }
        val i3  = urlList.getOrElse(2) { "" }
        val i4  = urlList.getOrElse(3) { "" }
        val i5  = urlList.getOrElse(4) { "" }
        val i6  = urlList.getOrElse(5) { "" }
        val i7  = urlList.getOrElse(6) { "" }
        val i8  = urlList.getOrElse(7) { "" }
        val i9  = urlList.getOrElse(8) { "" }
        val i10 = urlList.getOrElse(9) { "" }
        return Images(email, id, i1, i2, i3, i4, i5, i6, i7, i8, i9, i10)
    }
    // Generate an id for new product before adding in database
    private fun generateId(): Int {
        var idi = 1
        for( id in 1..dbProducts.num_of_row()){
            if (dbProducts.checkIf_ID_Exists(idi)) {
                idi ++
            }
            else
                break
        }

        return idi
    }
    // fil image RecyclerView
    fun adapter(){
        val list: RecyclerView = findViewById(R.id.list_imgs)
        val adapter = ImageAdapter(urlList)
        list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        list.adapter = adapter
        //adapter.notifyDataSetChanged()

    }
    // get Url from the fragment
    fun addUrl() {
        supportFragmentManager.setFragmentResultListener("requestKey", this) { _, bundle ->
            val url : String? = bundle.getString("url_key")
            if (!url.isNullOrBlank()) {
                urlList.add(url)
                Toast.makeText(this, "URL is added ", Toast.LENGTH_SHORT).show()
                adapter()
            } else {
                Toast.makeText(this, "Enter URL", Toast.LENGTH_SHORT).show()
            }
        }
    }
    // Widgets
    private fun initWidgets(){
        // init the widgets
        xName             = findViewById(R.id.productName)
        xDescription      = findViewById(R.id.description)
        xCategory         = findViewById(R.id.category)
        xBrand            = findViewById(R.id.brand)
        xpriceText        = findViewById(R.id.price)
        xquantityText     = findViewById(R.id.quantity)
        xweightText       = findViewById(R.id.weight)
        xdimensionText    = findViewById(R.id.dimensions)
        xshippingCostText = findViewById(R.id.shippingCost)
        xstockText        = findViewById(R.id.stockLevel)
        xminThresholdText = findViewById(R.id.minThreshold)
        xremiseText       = findViewById(R.id.remise2)
        xaddImgButton     = findViewById(R.id.uploadImages)
        xsaveButton       = findViewById(R.id.savePro)
    }
    // Focus on EditText if it Blank
    private fun emptyorblank() : Boolean{
        // convert the List to string to check if it is not null or blank
        val images = urlList.joinToString(",")
        if (images.isBlank()) {
            Toast.makeText(this, "Please upload at least one image", Toast.LENGTH_SHORT).show()
            return true
        }
        // if EditText Empty Focus on it
        if (xName.text.isNullOrBlank()) {
            xName.requestFocus()
            Toast.makeText(this, "Please enter a product name", Toast.LENGTH_SHORT).show()
            return true
        }
        if (xpriceText.text.isNullOrBlank()) {
            xpriceText.requestFocus()
            Toast.makeText(this, "Please enter a price", Toast.LENGTH_SHORT).show()
            return true
        }
        if (xquantityText.text.isNullOrBlank()) {
            xquantityText.requestFocus()
            Toast.makeText(this, "Please enter a quantity", Toast.LENGTH_SHORT).show()
            return true
        }
        if (xstockText.text.isNullOrBlank()) {
            xstockText.requestFocus()
            Toast.makeText(this, "Please enter a stock level", Toast.LENGTH_SHORT).show()
            return true
        }
        return false
    }
}
