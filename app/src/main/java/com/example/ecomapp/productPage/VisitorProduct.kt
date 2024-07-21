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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.R
import com.example.ecomapp.database.Image_DB
import com.example.ecomapp.database.Products_DB
import com.example.ecomapp.database.Rate_DB
import com.example.ecomapp.database.Rates
import com.example.ecomapp.Adapter_images
import com.example.ecomapp.inscrip.LogIn
import com.example.ecomapp.inscrip.SignUp
import com.example.ecomapp.UserSessionManager
import com.example.ecomapp.VisitorHome

class VisitorProduct : AppCompatActivity() {
    private val urlList = ArrayList<String>()
    private  var id:Int = 1000000
    private var sellerEmail:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_page)
        val backButton: ImageButton = findViewById(R.id.goback)

        setupMoreButton()
        backButton.setOnClickListener {
            onBackPressed()
        }

        id = intent.getIntExtra("productId", 1000000)
        sellerEmail = intent.getStringExtra("sellerEmail").toString()

        val db    = Products_DB(applicationContext)
        val dbImg = Image_DB(applicationContext)

        val products = db.getAllSellerProducts(sellerEmail)

        if (id != 1000000) {
            val images = dbImg.getAllProductImages(id, sellerEmail)
            for (img in images) {
                urlList.clear()
                urlList.addAll(images)
                urlList.removeAll { it.isBlank() }
                adapterImg()
                break
            }
        }
        for (p in products){
            if (p.Id == id){
                findViewById<TextView>(R.id.nom).setText("  ${p.Name}")
                findViewById<TextView>(R.id.prices).setText("  ${p.Price} DH")
                findViewById<TextView>(R.id.remise).setText("  ${p.remise} %")
                findViewById<TextView>(R.id.livrer).setText("  Livreson : "+p.Sh_Cost + " DH par tout Le Maroc")
                findViewById<TextView>(R.id.rateT).setText("  ${p.review}/5")
                findViewById<RatingBar>(R.id.ratingx).rating = p.review.toFloat()
                findViewById<TextView>(R.id.nbr_revs).setText("  ${p.QAcheter} clients Acheter se Produit ")
                findViewById<TextView>(R.id.tv_description).setText("  ${p.Description}")
            }
        }
        val add = findViewById<Button>(R.id.add)
        val addRate = findViewById<Button>(R.id.openRateFrgmt)
        loadRates()
        add.setOnClickListener {
            val i = Intent(this, LogIn::class.java)
            startActivity(i)
        }
        addRate.setOnClickListener {
            val i = Intent(this, LogIn::class.java)
            startActivity(i)
        }
    }

    private fun loadRates() {
        val list: RecyclerView = findViewById(R.id.list_rates)
        list.layoutManager = LinearLayoutManager(this)
        val db = Rate_DB(applicationContext)
        var listR = arrayListOf<Rates>()
        var totalR = 0.0
        val rates = db.getAllRates()
        for (r in rates){
            if (r.id == id){
                listR.add(r)
                totalR += r.rates
            }
        }
        totalR /= listR.size
        val db1 = Products_DB(applicationContext)
        db1.modifyReviews(id, sellerEmail, totalR)
        val adapter = Adapter_Rate("vide",this, listR)
        list.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun adapterImg(){
        val list: RecyclerView = findViewById(R.id.imgs)
        val adapter = Adapter_images(urlList)
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
        popupMenu.menuInflater.inflate(R.menu.main_menu1, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    val i = Intent(this, VisitorHome::class.java)
                    startActivity(i)
                    true
                }
                R.id.menu_account -> {
                    val i = Intent(this, LogIn::class.java)
                    startActivity(i)
                    true
                }
                R.id.menu_signup -> {
                    val session = UserSessionManager(applicationContext)
                    session.logoutUser()
                    val i = Intent(this, SignUp::class.java)
                    startActivity(i)
                    true
                }
                R.id.menu_sell -> {
                    val i = Intent(this, LogIn::class.java)
                    startActivity(i)
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }
}