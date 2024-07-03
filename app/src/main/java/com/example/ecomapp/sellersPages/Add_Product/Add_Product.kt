package com.example.ecomapp.to_sell.Add_product

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
import com.example.ecomapp.to_sell.Selling

class Add_Product : AppCompatActivity(), Url_new {

    private val urlList = mutableListOf<String>()
    private var sel: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_product)

        val sharedPref = applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val email = sharedPref.getString("email", null) ?: ""

        val db = Products_DB(applicationContext)
        val dbImg = Image_DB(applicationContext)

        val xName = findViewById<EditText>(R.id.productName)
        val xDescription = findViewById<EditText>(R.id.description)
        val spinner = findViewById<Spinner>(R.id.category)
        val xBrand = findViewById<EditText>(R.id.brand)
        val priceText = findViewById<EditText>(R.id.price)
        val quantityText = findViewById<EditText>(R.id.quantity)
        val weightText = findViewById<EditText>(R.id.weight)
        val dimensionText = findViewById<EditText>(R.id.dimensions)
        val shippingCostText = findViewById<EditText>(R.id.shippingCost)
        val stockText = findViewById<EditText>(R.id.stockLevel)
        val minThresholdText = findViewById<EditText>(R.id.minThreshold)
        val remiseText = findViewById<EditText>(R.id.remise2)
        val addImgButton = findViewById<Button>(R.id.uploadImages)
        val saveButton: Button = findViewById(R.id.savePro)

        // Category Spinner
        categorys()
        var x = 0.0
        // Retrieve product details if editing existing product
        val id = intent.getIntExtra("productId", 1000000)
        if (id != 1000000) {
            val products = db.getAll_S(email)
            val images = dbImg.getAll_iP(id, email)

            for (p in products) {
                if (p.Id == id) {
                    setProduct(p)
                    urlList.clear()
                    for (img in images) {
                        urlList.addAll(listOf(img.img1, img.img2, img.img3, img.img4, img.img5, img.img6, img.img7, img.img8, img.img9, img.img10))
                    }
                    urlList.removeAll { it.isBlank() }
                    adapter()
                    break
                }
            }
            x = 3000.95
        }

        // Upload Images Button
        addImgButton.setOnClickListener {
            val fragment: Fragment = BlankFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container1, fragment)
                .addToBackStack(null)
                .commit()
        }


        // Save Button Click Listener
        saveButton.setOnClickListener {
            val category = categorys()
            val images = urlList.joinToString(",")
        //if Empty Focus ---------------------------------------------------------------------------
            if (xName.text.isNullOrBlank()) {
                xName.requestFocus()
                Toast.makeText(this, "Please enter a product name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (priceText.text.isNullOrBlank()) {
                priceText.requestFocus()
                Toast.makeText(this, "Please enter a price", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (quantityText.text.isNullOrBlank()) {
                quantityText.requestFocus()
                Toast.makeText(this, "Please enter a quantity", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (stockText.text.isNullOrBlank()) {
                stockText.requestFocus()
                Toast.makeText(this, "Please enter a stock level", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (images.isNullOrBlank()) {
                Toast.makeText(this, "Please upload at least one image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (x==0.0){
                val idp = generateId()

                val newProduct = toaddProduct(email, idp, category, images)
                val newImages = addImages(email, idp)

                db.add_Product(newProduct)
                dbImg.addIMg(newImages)

                Toast.makeText(this, "Product is saved", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Selling::class.java))
            }
            else{

                val upProduct = toaddProduct(email, id, category, images)
                db.update(upProduct,id,email)
                val upImages = addImages(email, id)
                dbImg.upIMg(upImages,id,email)

                startActivity(Intent(this, Selling::class.java))

            }

        }
    }

    private fun setProduct(product: Product) {
        // Populate UI fields with product details
        val xName = findViewById<EditText>(R.id.productName)
        val xDescription = findViewById<EditText>(R.id.description)
        val spinner = findViewById<Spinner>(R.id.category)
        val xBrand = findViewById<EditText>(R.id.brand)
        val priceText = findViewById<EditText>(R.id.price)
        val quantityText = findViewById<EditText>(R.id.quantity)
        val weightText = findViewById<EditText>(R.id.weight)
        val dimensionText = findViewById<EditText>(R.id.dimensions)
        val shippingCostText = findViewById<EditText>(R.id.shippingCost)
        val stockText = findViewById<EditText>(R.id.stockLevel)
        val minThresholdText = findViewById<EditText>(R.id.minThreshold)
        val remiseText = findViewById<EditText>(R.id.remise2)

        xName.setText(product.Name)
        xDescription.setText(product.Description)
        xBrand.setText(product.Brand)
        spinnerCat(spinner, product.Category)
        priceText.setText(product.Price.toString())
        shippingCostText.setText(product.Sh_Cost.toString())
        stockText.setText(product.Stock_Level.toString())
        quantityText.setText(product.Quantity.toString())
        minThresholdText.setText(product.Min_Threshold.toString())
        weightText.setText(product.Weight.toString())
        dimensionText.setText(product.dimension.toString())
        remiseText.setText(product.remise.toString())


    }

    private fun categorys(): String {
        val categorys: Spinner = findViewById(R.id.category)
        val choixCtg = listOf("Select Category", "Men", "Women", "Kids")
        val textColor = resources.getColor(R.color.black)

        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            choixCtg,

        )

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorys.adapter = spinnerAdapter

        categorys.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                sel = when (position) {
                    0 -> "Select Category"
                    1 -> "Men"
                    2 -> "Women"
                    else -> "Kids"
                }
                (view as? TextView)?.setTextColor(textColor)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        return sel
    }

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


    private fun toaddProduct(email: String, id: Int, category: String, images: String): Product {
        val name = findViewById<EditText>(R.id.productName).text.toString().let {
            if (it.isEmpty()) "" else it
        }
        val description = findViewById<EditText>(R.id.description).text.toString().let {
            if (it.isEmpty()) "" else it
        }
        val brand = findViewById<EditText>(R.id.brand).text.toString().let {
            if (it.isEmpty()) "" else it
        }
        val price = findViewById<EditText>(R.id.price).text.toString().toDoubleOrNull() ?: 0.0
        val quantity = findViewById<EditText>(R.id.quantity).text.toString().toIntOrNull() ?: 0
        val weight = findViewById<EditText>(R.id.weight).text.toString().toDoubleOrNull() ?: 0.0
        val dimension = findViewById<EditText>(R.id.dimensions).text.toString().toDoubleOrNull() ?: 0.0
        val shippingCost = findViewById<EditText>(R.id.shippingCost).text.toString().toDoubleOrNull() ?: 0.0
        val stockLevel = findViewById<EditText>(R.id.stockLevel).text.toString().toIntOrNull() ?: 0
        val minThreshold = findViewById<EditText>(R.id.minThreshold).text.toString().toIntOrNull() ?: 0
        val remise = findViewById<EditText>(R.id.remise2).text.toString().toIntOrNull() ?: 0

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
            images,
            remise,
            0.0,
            "No Sell"
        )
    }

    private fun addImages(email: String, id: Int): Images {
        val i1 = urlList.getOrElse(0) { "" }
        val i2 = urlList.getOrElse(1) { "" }
        val i3 = urlList.getOrElse(2) { "" }
        val i4 = urlList.getOrElse(3) { "" }
        val i5 = urlList.getOrElse(4) { "" }
        val i6 = urlList.getOrElse(5) { "" }
        val i7 = urlList.getOrElse(6) { "" }
        val i8 = urlList.getOrElse(7) { "" }
        val i9 = urlList.getOrElse(8) { "" }
        val i10 = urlList.getOrElse(9) { "" }

        return Images(email, id, i1, i2, i3, i4, i5, i6, i7, i8, i9, i10)
    }

    private fun generateId(): Int {
        val db = Products_DB(applicationContext)
        var id_i = 0
        for (i in 0 until db.num_of_row()) {
            if (db.checkIf_ID_Exists(id_i)) {
                id_i += 1
            }
        }
        return id_i
    }

    fun adapter(){
        val list: RecyclerView = findViewById(R.id.list_imgs)
        val adapter = Adapter_imgs(this, urlList)
        list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        list.adapter = adapter
        adapter.notifyDataSetChanged()

    }
    override fun getUrl(url: String) {
        if (url.isNotBlank()) {
            urlList.add(url)
            Toast.makeText(this, "URL is added ", Toast.LENGTH_SHORT).show()
            adapter()
        } else {
            Toast.makeText(this, "Enter URL", Toast.LENGTH_SHORT).show()
        }
    }
}
