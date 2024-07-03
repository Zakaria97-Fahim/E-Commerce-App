package com.example.ecomapp.productPage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.R
import com.example.ecomapp.customer_home.Home
import com.example.ecomapp.database.Image_DB
import com.example.ecomapp.database.Product
import com.example.ecomapp.database.Products_DB
import com.example.ecomapp.database.Rate_DB
import com.example.ecomapp.database.Rates
import com.example.ecomapp.home_pour_visiteur.Adapter_images
import com.example.ecomapp.home_pour_visiteur.Home_pour_Visiteur
import com.example.ecomapp.inscrip.UserSessionManager
import com.example.ecomapp.toBuy.Panier
import com.example.ecomapp.sellersPages.Selling

class Product_Pour_Customer : AppCompatActivity() {
    private val urlList = mutableListOf<String>()
    private lateinit var db:Products_DB
    private lateinit var dbImg:Image_DB

    private var id:Int = 1000000
    private var sellerEmail:String = ""
    private var userEmail:String = "shared"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_page)
        val backButton: ImageButton = findViewById(R.id.goback)
        val add = findViewById<Button>(R.id.add)
        val addRate = findViewById<Button>(R.id.openRateFrgmt)
        val PanierButton: ImageButton = findViewById(R.id.pannier)

        setupMoreButton()
        backButton.setOnClickListener {
            onBackPressed()
        }
        PanierButton.setOnClickListener {
            addToPanier()
        }

        db = Products_DB(applicationContext)
        dbImg = Image_DB(applicationContext)

        val sharedPref = applicationContext.getSharedPreferences("MyPrefs", MODE_PRIVATE)

        userEmail = sharedPref.getString("email", null) ?: ""
        id = intent.getIntExtra("productId", 1000000)
        sellerEmail = intent.getStringExtra("sellerEmail").toString()


        adapterImg()

        val products = db.getAll_S(sellerEmail)
        for (p in products){
            if (p.Id == id){
                findViewById<TextView>(R.id.nom).text = "  ${p.Name}"
                findViewById<TextView>(R.id.prices).text = " ${p.Price} DH"
                findViewById<TextView>(R.id.remise).text = "  ${p.remise} %"
                findViewById<RatingBar>(R.id.ratingx).rating = p.review.toFloat()
                findViewById<TextView>(R.id.livrer).text = "  Delivery : "+ p.Sh_Cost +" DH"
                findViewById<TextView>(R.id.rateT).text = "  ${p.review}/5 Rate this "
                findViewById<TextView>(R.id.nbr_revs).text = "  ${p.QAcheter} clients buy this Products "
                findViewById<TextView>(R.id.tv_description).text = "  ${p.Description}"
            }
        }

        add.setOnClickListener {
            addToPanier()
        }
        addRate.setOnClickListener {
            if (userEmail != sellerEmail){
                val fragment = Rate_Fragment.newInstance(id, sellerEmail)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.containerRate, fragment)
                    .addToBackStack(null)
                    .commit()
            }else{
                Toast.makeText(this, "You Can't Rate Your Products !!!", Toast.LENGTH_SHORT).show()
            }

        }
        loadRates()

    }
    private fun addToPanier(){
        val product: Product? = db.getAll().find { it.Email == sellerEmail }
        if (product != null) {
            db.modify(product.Id, product.Email, "Wait Confirmation")
            val intent = Intent(applicationContext, Panier::class.java)
            intent.putExtra("productId", id)
            intent.putExtra("sellerEmail", sellerEmail )
            startActivity(intent)
        }
    }

    private fun loadRates() {
        var totalR = 0.0
        val dbRate = Rate_DB(applicationContext)
        var listR = arrayListOf<Rates>()
        val rates = dbRate.getAll()

        for (r in rates){
            if (r.id == id){
                listR.add(r)
                totalR += r.rates
            }
        }

        findViewById<TextView>(R.id.rateT).setText("  ${listR.size} Rate this ")

        totalR /= listR.size
        db.modifyReviews(id, sellerEmail, totalR)
        findViewById<RatingBar>(R.id.ratingx).rating = totalR.toFloat()
        val adapter = Adapter_Rate(userEmail ,this, listR)

        val list: RecyclerView = findViewById(R.id.list_rates)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun adapterImg(){
        if (id != 1000000) {
            val images = dbImg.getAll_iP(id, sellerEmail)
            for (img in images) {
                urlList.clear()
                urlList.addAll(listOf(img.img1, img.img2, img.img3, img.img4, img.img5, img.img6, img.img7, img.img8, img.img9, img.img10))
                urlList.removeAll { it.isBlank() }
                break
            }
        }
        val list: RecyclerView = findViewById(R.id.imgs)
        val adapter = Adapter_images(this, urlList)
        list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        list.adapter = adapter
        adapter.notifyDataSetChanged()

    }
    //Display Menu of Settings========================================================================
    private fun setupMoreButton() {
        val moreButton: ImageButton = findViewById(R.id.settings)

        moreButton.setOnClickListener { view ->
            showPopupMenu(view)
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