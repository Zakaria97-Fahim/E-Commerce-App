package com.example.ecomapp.visitorHome

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.database.Product
import com.example.ecomapp.R
import com.example.ecomapp.database.Image_DB
import com.example.ecomapp.database.Products_DB
import com.example.ecomapp.inscrip.LogIn
import com.example.ecomapp.productPage.VisitorProduct
import com.squareup.picasso.Picasso

class VisitorAdapter(private val context: Context, private val dataList: ArrayList<Product>) :
    RecyclerView.Adapter<VisitorAdapter.ViewHolder>() {
    val dbProduct = Products_DB(context)
    val dbImg     = Image_DB(context)

    // Create new item of RecyclerView, and put on it the CardView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_home, parent, false)
        return ViewHolder(view)
    }
    // try to remove inner !!!
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imag        : ImageView     = itemView.findViewById(R.id.image1)
        val name        : TextView      = itemView.findViewById(R.id.name1)
        val price       : TextView      = itemView.findViewById(R.id.price1)
        val remise      : TextView      = itemView.findViewById(R.id.remise1)
        val reviews     : RatingBar     = itemView.findViewById(R.id.review1)
        val addtoCart : Button        = itemView.findViewById(R.id.add1)
        init {

            // Add to Cart
            addtoCart.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = dataList[position]
                    //db.modifyConfirmation(product.Id, product.sellerEmail, "Wait Confirmation")
                    val intent = Intent(context, LogIn::class.java)
                    context.startActivity(intent)
                }
            }
            // went to Product Page by clickong on the item
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = dataList[position]
                    val intent = Intent(context, VisitorProduct::class.java)
                    intent.putExtra("productId", product.Id)
                    intent.putExtra("sellerEmail", product.sellerEmail)
                    context.startActivity(intent)
                }
            }
        }
    }

    // Fill the Card Home using dataList<Product>
    /*
       we can use holder.addtoPanier.setOnClickListener{} or other interaction
       with the Item of the  RecyclerView , instead using init{}
    */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val product = dataList[position]

        holder.name.text      = product.Name
        holder.price.text     = "${product.Price} DH"
        holder.remise.text    = "- ${product.remise} %"
        holder.reviews.rating = product.review.toFloat()

        // get all images of the product
        val img = dbImg.getAllProductImages(product.Id, product.sellerEmail)
        Picasso.get()
            .load(img[0])
            .into(holder.imag)
    }
    // get number of Products
    override fun getItemCount(): Int {
        return dataList.size
    }
}
