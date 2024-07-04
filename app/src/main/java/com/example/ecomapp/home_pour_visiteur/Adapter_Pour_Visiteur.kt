package com.example.ecomapp.home_pour_visiteur

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
import com.example.ecomapp.inscrip.Log_in
import com.example.ecomapp.productPage.Product_Pour_Visiteur
import com.squareup.picasso.Picasso

class Adapter_Pour_Visiteur(private val context: Context, private val dataList: MutableList<Product>) :
    RecyclerView.Adapter<Adapter_Pour_Visiteur.ViewHolder>() {

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
        val add: Button =  itemView.findViewById(R.id.add1)

        init {
            imag.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = dataList[position]
                    val intent = Intent(context, Product_Pour_Visiteur::class.java)
                    intent.putExtra("productId", product.Id)
                    intent.putExtra("sellerEmail", product.Email)
                    context.startActivity(intent)
                }
            }
            add.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val intent = Intent(context, Log_in::class.java)
                    context.startActivity(intent)
                }
            }
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = dataList[position]
                    val intent = Intent(context, Product_Pour_Visiteur::class.java)
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
        holder.remise.text = "${Product.remise} %"
        holder.reviews.rating = Product.review.toFloat()

        Picasso.get()
            .load(Product.images)
            .into(holder.imag)
    }
    override fun getItemCount(): Int {
        return dataList.size
    }


}
