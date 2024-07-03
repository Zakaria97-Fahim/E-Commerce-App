package com.example.ecomapp.customer_home

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
import com.example.ecomapp.database.Products_DB
import com.example.ecomapp.productPage.Product_Pour_Customer
import com.example.ecomapp.toBuy.Panier
import com.squareup.picasso.Picasso

class Home_Adapter_card(private val context: Context, private val dataList: MutableList<Product>) :
    RecyclerView.Adapter<Home_Adapter_card.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_home, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imag: ImageView = itemView.findViewById(R.id.image1)
        val name: TextView = itemView.findViewById(R.id.name1)
        val price: TextView = itemView.findViewById(R.id.price1)
        val remise: TextView = itemView.findViewById(R.id.remise1)
        val reviews: RatingBar = itemView.findViewById(R.id.review1)
        val addtoPanier: Button =  itemView.findViewById(R.id.add1)


        init {
            imag.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = dataList[position]
                    val intent = Intent(context, Product_Pour_Customer::class.java)
                    intent.putExtra("productId", product.Id)
                    intent.putExtra("sellerEmail", product.Email)
                    context.startActivity(intent)
                }
            }
            addtoPanier.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = dataList[position]
                    val db = Products_DB(context)
                    db.modify(product.Id, product.Email, "Wait Confirmation")
                    val intent = Intent(context, Panier::class.java)
                    intent.putExtra("productId", product.Id)
                    intent.putExtra("sellerEmail", product.Email)
                    context.startActivity(intent)
                }
            }

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
        holder.price.text = "${Product.Price}"
        holder.remise.text = "- ${Product.remise} %"
        holder.reviews.rating = Product.review.toFloat()

        Picasso.get()
            .load(Product.images)
            .into(holder.imag)
    }
    override fun getItemCount(): Int {
        return dataList.size
    }
}
