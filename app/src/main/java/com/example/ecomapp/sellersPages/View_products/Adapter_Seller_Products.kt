package com.example.ecomapp.to_sell.View_products

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
import com.example.ecomapp.database.Products_DB
import com.example.ecomapp.R
import com.example.ecomapp.database.Image_DB
import com.example.ecomapp.database.PanierProducts_DB
import com.example.ecomapp.database.Rate_DB
import com.example.ecomapp.productPage.Product_Pour_Customer
import com.example.ecomapp.to_sell.Add_product.Add_Product
import com.squareup.picasso.Picasso

class Adapter_Seller_Products(private val userE:String, private val context: Context, private val dataList: MutableList<Product>) :
    RecyclerView.Adapter<Adapter_Seller_Products.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_seller_products, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imag: ImageView = itemView.findViewById(R.id.image3)
        val name: TextView = itemView.findViewById(R.id.name3)
        val price: TextView = itemView.findViewById(R.id.price3)
        val delete3: Button =  itemView.findViewById(R.id.delete3)
        val modifier: Button =  itemView.findViewById(R.id.modify3)
        val review: RatingBar = itemView.findViewById(R.id.review3)
        val quantity: TextView = itemView.findViewById(R.id.quantity3)
        val quantityT: TextView = itemView.findViewById(R.id.quantityT)

        var id : Int = 0
        var email : String = null.toString()

        init {
            // Set up click listener for the delete button in the ViewHolder
            delete3.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    removeCard(position)
                    val db1 = Products_DB(context)
                    val db2 = Image_DB(context)
                    val db3 = PanierProducts_DB(context)
                    val db4 = Rate_DB(context)
                    db1.delete(id, email)
                    db2.delete(id, email)
                    db3.delete(id, email)
                    db4.deleteRate(id, email, userE)

                }
            }
            modifier.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = dataList[position]
                    val intent = Intent(context, Add_Product::class.java)
                    intent.putExtra("productId", id)
                    intent.putExtra("email", email)
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
        holder.price.text = "${Product.Price} DH"
        holder.review.rating = Product.review.toFloat()
        holder.quantity.text = "${Product.QRest}"
        holder.quantityT.text = "${Product.Quantity}"

        holder.id = Product.Id
        holder.email = Product.Email
        Picasso.get()
            .load(Product.images)
            .into(holder.imag)
    }

    fun removeCard(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


}
