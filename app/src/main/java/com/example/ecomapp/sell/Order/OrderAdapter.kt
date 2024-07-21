package com.example.ecomapp.sell.Order

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.database.Product
import com.example.ecomapp.R
import com.example.ecomapp.database.Image_DB
import com.example.ecomapp.productPage.CustomerProduct
import com.squareup.picasso.Picasso

class OrderAdapter(private val context: Context, private val dataList: MutableList<Product>) :
    RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    val db = Image_DB(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_order, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Get the Widget
        val imag     : ImageView = itemView.findViewById(R.id.imageOrder)
        val name     : TextView  = itemView.findViewById(R.id.nameOrder)
        val price    : TextView  = itemView.findViewById(R.id.priceOrder)
        val review   : RatingBar = itemView.findViewById(R.id.reviewOrder)
        val quantity : TextView  = itemView.findViewById(R.id.quantityOrder)

        var id    : Int    = 0
        var email : String = null.toString()

        init {
            // go to Product Page
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // get the position of the product
                    val product = dataList[position]
                    val intent  = Intent(context, CustomerProduct::class.java)
                    // send productId and sellerEmail
                    intent.putExtra("productId", product.Id)
                    intent.putExtra("sellerEmail", product.sellerEmail)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val product = dataList[position]

        holder.name.text     = product.Name
        holder.price.text    = "${product.Price} DH"
        holder.review.rating = product.review.toFloat()
        holder.quantity.text = "${product.QAcheter} Sold"

        holder.id    = product.Id
        holder.email = product.sellerEmail
        // get all Product images
        val img = db.getAllProductImages(holder.id, holder.email)

        Picasso.get()
            .load(img[0])
            .into(holder.imag)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
