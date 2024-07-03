package com.example.ecomapp.toBuy

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
import com.example.ecomapp.database.PanierProducts_DB
import com.example.ecomapp.productPage.Product_Pour_Customer
import com.example.ecomapp.database.Products_DB
import com.squareup.picasso.Picasso

class Adapter_Panier_Cards(private val context: Context, private val dataList: MutableList<Product>) :
    RecyclerView.Adapter<Adapter_Panier_Cards.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_pannier, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imag: ImageView = itemView.findViewById(R.id.image2)
        val name: TextView = itemView.findViewById(R.id.name2)
        val price: TextView = itemView.findViewById(R.id.pricePanier)
        val remise: TextView = itemView.findViewById(R.id.remise2)
        val reviews: RatingBar = itemView.findViewById(R.id.review2)
        val quantite: TextView = itemView.findViewById(R.id.quantity2)
        var quant = quantite.text.toString().toInt()
        val buy: Button =  itemView.findViewById(R.id.buy2)
        val delete2: Button =  itemView.findViewById(R.id.delete2)
        val moinsQ: ImageButton =  itemView.findViewById(R.id.moins2)
        val plusQ: ImageButton =  itemView.findViewById(R.id.plus2)

        var id : Int = 0
        var email : String = null.toString()

        init {
            plusQ.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = dataList[position]
                    val rest = product.QRest
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
            moinsQ.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = dataList[position]
                    val rest = product.QRest
                    if (5<rest){
                        if (quant>1 ){ quant -= 1 }else{ quant = 1}
                    }
                    else if(rest == 0){
                        quant = 0
                        Toast.makeText(context,"This Product is not Available",Toast.LENGTH_SHORT).show()
                    }

                    quantite.text = "$quant"
                }
            }
            imag.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = dataList[position]
                    val intent = Intent(context, Product_Pour_Customer::class.java)
                    intent.putExtra("productId", id)
                    intent.putExtra("selleEmail", email)
                    context.startActivity(intent)
                }
            }
            delete2.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val db1 = Products_DB(context)
                    db1.modify(id, email, "No Sell")
                    removeCard(position)
                    val db = PanierProducts_DB(context)
                    db.delete(id, email)
                }
            }
            buy.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = dataList[position]
                    val intent = Intent(context, ToPaid::class.java)
                    intent.putExtra("productId", product.Id)
                    intent.putExtra("sellerEmail", product.Email)
                    intent.putExtra("prix", product.Price)
                    intent.putExtra("quantite", quant)
                    intent.putExtra("remise", product.remise)
                    context.startActivity(intent)
                }
            }
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = dataList[position]
                    val intent = Intent(context, Product_Pour_Customer::class.java)
                    intent.putExtra("productId", product.Id)
                    intent.putExtra("email", product.Email)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val Product = dataList[position]

        holder.name.text = "${Product.Name}"
        holder.price.text = "${Product.Price}"
        holder.remise.text = "- ${Product.remise} %"
        holder.reviews.rating = Product.review.toFloat()
        holder.id = Product.Id
        holder.email = Product.Email

        Picasso.get()
            .load(Product.images)
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
