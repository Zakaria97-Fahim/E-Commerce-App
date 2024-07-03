package com.example.ecomapp.sellersPages.Order

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
import com.example.ecomapp.productPage.Product_Pour_Customer
import com.squareup.picasso.Picasso

class Adapter_card_order(private val context: Context, private val dataList: MutableList<Product>) :
    RecyclerView.Adapter<Adapter_card_order.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_order, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imag: ImageView = itemView.findViewById(R.id.imageOrder)
        val name: TextView = itemView.findViewById(R.id.nameOrder)
        val price: TextView = itemView.findViewById(R.id.priceOrder)
        val review: RatingBar = itemView.findViewById(R.id.reviewOrder)
        val quantity: TextView = itemView.findViewById(R.id.quantityOrder)
        var id : Int = 0
        var email : String = null.toString()
        init {
            itemView.setOnClickListener {

                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = dataList[position]
                    val intent = Intent(context, Product_Pour_Customer::class.java)
                    intent.putExtra("productId", product.Id)
                    intent.putExtra("sellerEmail", product.Email)
                    context.startActivity(intent)
                }
            }
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val Product = dataList[position]

        holder.name.text = Product.Name
        holder.price.text = "${Product.Price} DH"
        holder.review.rating = Product.review.toFloat()
        holder.quantity.text = " ${Product.QAcheter} Selled "

        holder.id = Product.Id
        holder.email = Product.Email
        Picasso.get()
            .load(Product.images)
            .into(holder.imag)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


}
