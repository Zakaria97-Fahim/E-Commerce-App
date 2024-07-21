package com.example.ecomapp.buy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import com.example.ecomapp.R
import com.example.ecomapp.customerHome.Home
import com.example.ecomapp.database.ProductsCart_DB
import com.example.ecomapp.database.Products_DB
import com.example.ecomapp.UserSessionManager
import com.example.ecomapp.VisitorHome
import com.example.ecomapp.sell.Selling
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class PaidPage : AppCompatActivity() {

    lateinit var bankName : EditText
    lateinit var cartNbr  : EditText
    lateinit var cartdate : EditText
    lateinit var nbr_3    : EditText
    lateinit var address  : EditText
    lateinit var phone    : EditText
    lateinit var prixT    : TextView


    lateinit var selleremail : String
    lateinit var clientEmail   : String

    var id    : Int = 0
    var quant : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.paid)

        // get database of Products
        val db  = Products_DB(applicationContext)
        val db2 = ProductsCart_DB(applicationContext)

        // back
        val backButton: ImageButton = findViewById(R.id.back)
        backButton.setOnClickListener { onBackPressed() }

        //settings
        setupMoreButton()

        // get the important Information
        selleremail = intent.getStringExtra("sellerEmail").toString()
        id          = intent.getIntExtra("productId",10000000)
        quant       = intent.getIntExtra("quantite",0)
        val prT     = intent.getDoubleExtra("prix",0.0)
        val remise  = intent.getIntExtra("remise",0)

        // Retrieve user email from SharedPreferences
        val sharedPref = applicationContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        clientEmail = sharedPref.getString("email", null) ?: ""

        //Widgets
        bankName = findViewById(R.id.holder)
        cartNbr  = findViewById(R.id.card_number)
        cartdate = findViewById(R.id.date)
        nbr_3    = findViewById(R.id.nbr3)
        phone    = findViewById(R.id.phone)
        address  = findViewById(R.id.Address)
        prixT    = findViewById(R.id.prixTotal)

        //calculer total Price must paid
        prixT.text = (prT * quant * (100-remise)/100).toString() + "DH"

<<<<<<< HEAD:app/src/main/java/com/example/ecomapp/buy/PaidPage.kt
=======
        prixT.text = (prT * quant * (100-remise)/100).toString() + " DH"
>>>>>>> e8073d14cd90b09bf2178d6d7a61515e85187473:app/src/main/java/com/example/ecomapp/toBuy/ToPaid.kt
        val save: Button = findViewById(R.id.save)

        save.setOnClickListener {
            val e1 = bankName.text.toString().trim()
            val e2 = cartNbr.text.toString().trim()
            val e3 = cartdate.text.toString().trim()
            val e4 = nbr_3.text.toString().trim()
            val e5 = phone.text.toString().trim()
            val e6 = address.text.toString().trim()

            focusOn(db, db2, e6, e5, e1, e2, e3, e4 )
        }

    }

    private fun focusOn(dbProduct: Products_DB, dbCart: ProductsCart_DB, e6: String, e5: String, e1: String, e2: String, e3: String, e4: String) {
        if (e6.isBlank()) {
            address.requestFocus()
            Toast.makeText(this, "Please enter a L'address pour Livraison", Toast.LENGTH_SHORT).show()
            return
        }
        if (e1.isBlank()) {
            bankName.requestFocus()
            Toast.makeText(this, "Please enter a bank name", Toast.LENGTH_SHORT).show()
            return
        }
        if (e2.length != 16 || !e2.matches("\\d+".toRegex())) {
            cartNbr.requestFocus()
            Toast.makeText(this, "Please enter a valid 16-digit card number", Toast.LENGTH_SHORT).show()
            return
        }
        if (!isValidDate(e3)) {
            cartdate.requestFocus()
            Toast.makeText(this, "Please enter a valid date \n DD/MM/YYYY", Toast.LENGTH_SHORT).show()
            return
        }
        if (e4.length != 3 || !e4.matches("\\d+".toRegex())) {
            nbr_3.requestFocus()
            Toast.makeText(this, "Please enter a valid 3-digit number", Toast.LENGTH_SHORT).show()
            return
        }
        if (e5.length != 10 || !e4.matches("\\d+".toRegex())) {
            phone.requestFocus()
            Toast.makeText(this, "Please enter a valid 10-digit phone_number", Toast.LENGTH_SHORT).show()
            return
        }

        // delete from Cart
        dbCart.deleteId(id,selleremail,clientEmail)
        // Not use it Yet !!!
        // dbProduct.modifyConfirmation(id, selleremail, "Confirmed")
        // dicrease total quantity of the products
        dbProduct.modifyQuantity(id,selleremail, quant)

        Toast.makeText(this, "Congratulation !!!", Toast.LENGTH_SHORT).show()

        //comeBack to Home Page
        val intent = Intent(this, Home::class.java)
        startActivity(intent)
    }

    // valid Date
    private fun isValidDate(date: String): Boolean {
        val pattern = "dd/MM/yyyy"
        val format = DateTimeFormatter.ofPattern(pattern)

        return try {
            LocalDate.parse(date, format)
            true
        } catch (e: DateTimeParseException) {
            false
        }
    }


    //Settings===================================================================================
    private fun setupMoreButton() {
        val moreButton: ImageButton = findViewById(R.id.settingsPaid)
        moreButton.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }
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
                    val i = Intent(this, ProductCart::class.java)
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
