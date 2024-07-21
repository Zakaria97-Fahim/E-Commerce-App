package com.example.ecomapp.buy

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.database.Product
import com.example.ecomapp.R
import com.example.ecomapp.database.Image_DB
import com.example.ecomapp.database.ProductsCart_DB
import com.example.ecomapp.productPage.CustomerProduct
import com.example.ecomapp.database.Products_DB
import com.squareup.picasso.Picasso

class AdapterCart(private val context: Context, private val dataList: MutableList<Product>) :
    RecyclerView.Adapter<AdapterCart.ViewHolder>() {

    val dbImage    = Image_DB(context)
    val dbCart     = ProductsCart_DB(context)
    val dbProducts = Products_DB(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_carts, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // get Widgets
        val imag     : ImageView   = itemView.findViewById(R.id.image2)
        val name     : TextView    = itemView.findViewById(R.id.name2)
        val price    : TextView    = itemView.findViewById(R.id.pricePanier)
        val remise   : TextView    = itemView.findViewById(R.id.remise2)
        val reviews  : RatingBar   = itemView.findViewById(R.id.review2)
        val quantite : TextView    = itemView.findViewById(R.id.quantity2)
        val buy      : Button      = itemView.findViewById(R.id.buy2)
        val delete  : Button      = itemView.findViewById(R.id.delete2)
        val moinsQ   : ImageButton = itemView.findViewById(R.id.moins2)
        val plusQ    : ImageButton = itemView.findViewById(R.id.plus2)

        var id : Int = 0
        var email : String = null.toString()
        var quant = quantite.text.toString().toInt()

        init {
            // increase Quantity to Buy
            plusQ.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = dataList[position]
                    val rest = product.QRest
                    // !
                    if (5<rest){
                        if (quant<5 ){ quant += 1 }else{ quant = 5}
                    }
                    else if(rest == 0){
                        quant = 0
                        Toast.makeText(context,"This Product is not Available",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        if (quant<rest ){ quant += 1 }else{ quant = rest}
                    }
                    quantite.text = "$quant"
                }
            }
            // decrease Quantity to Buy
            moinsQ.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = dataList[position]
                    // get the rest of the Product
                    val rest = product.QRest

                    if (0<rest){
                        if (quant>1 ){ quant -= 1 }else{ quant = 1}
                    }
                    else if(rest == 0){
                        quant = 0
                        Toast.makeText(context,"This Product is not Available",Toast.LENGTH_SHORT).show()
                    }
                    quantite.text = "$quant"
                }
            }
            // open Product Page by clicking on the image
            imag.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = dataList[position]
                    val intent = Intent(context, CustomerProduct::class.java)
                    intent.putExtra("productId", product.Id)
                    intent.putExtra("selleEmail", product.sellerEmail)
                    context.startActivity(intent)
                }
            }
            // remove the Product from the Cart
            delete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    //dbProducts.modifyConfirmation(id, email, "No Sell")
                    // remove from cart
                    removeCard(position)
                    // delete from card database
                    dbCart.deleteFromCart(id, email)
                }
            }
            // went to Paid Page to buy the product
            buy.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = dataList[position]
                    val intent = Intent(context, PaidPage::class.java)
                    intent.putExtra("productId", product.Id)
                    // !
                    intent.putExtra("sellerEmail", product.sellerEmail)
                    intent.putExtra("prix", product.Price)
                    intent.putExtra("quantite", quant)
                    intent.putExtra("remise", product.remise)
                    context.startActivity(intent)
                }
            }
            // went to ProductPage, when item selected
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = dataList[position]
                    val intent = Intent(context, CustomerProduct::class.java)
                    intent.putExtra("productId", product.Id)
                    intent.putExtra("email", product.sellerEmail)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val Product = dataList[position]
        // fill the widget
        holder.name.text      = "${Product.Name}"
        holder.price.text     = "${Product.Price}"
        holder.remise.text    = "- ${Product.remise} %"
        holder.reviews.rating = Product.review.toFloat()

        holder.id    = Product.Id
        holder.email = Product.sellerEmail

        // get all Product images
        val img = dbImage.getAllProductImages(holder.id, holder.email)

        Picasso.get()
            .load(img[0])
            .into(holder.imag)
    }
    override fun getItemCount(): Int {
        return dataList.size
    }

    fun removeCard(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }
}
