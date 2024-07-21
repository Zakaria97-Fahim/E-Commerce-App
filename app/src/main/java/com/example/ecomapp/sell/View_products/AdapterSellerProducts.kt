package com.example.ecomapp.sell.View_products

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
import com.example.ecomapp.database.ProductsCart_DB
import com.example.ecomapp.database.Rate_DB
import com.example.ecomapp.productPage.CustomerProduct
import com.example.ecomapp.sell.AddProduct.AddProduct
import com.squareup.picasso.Picasso

class AdapterSellerProducts(private val userE:String, private val context: Context, private val dataList: MutableList<Product>) :
    RecyclerView.Adapter<AdapterSellerProducts.ViewHolder>() {
    val db1 = Products_DB(context)
    val db2 = Image_DB(context)
    val db3 = ProductsCart_DB(context)
    val db4 = Rate_DB(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_seller_products, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // get the Widget
        val imag      : ImageView = itemView.findViewById(R.id.image3)
        val name      : TextView  = itemView.findViewById(R.id.name3)
        val price     : TextView  = itemView.findViewById(R.id.price3)
        val delete    : Button    = itemView.findViewById(R.id.delete3)
        val modifier  : Button    = itemView.findViewById(R.id.modify3)
        val review    : RatingBar = itemView.findViewById(R.id.review3)
        val quantity  : TextView  = itemView.findViewById(R.id.quantity3)
        val quantityT : TextView  = itemView.findViewById(R.id.quantityT)

        var id : Int = 0
        var email : String = null.toString()

        init {
            // delete The Product From All DataBases
            delete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // delete the card item from RecyclerView
                    removeCard(position)
                    // delete from all DataBases
                    db1.deleteProduct(id, email)
                    db2.deleteIMG(id, email)
                    db3.deleteFromCart(id, email)
                    db4.deleteRate(id, email, userE)

                }
            }
            modifier.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // went to AddProduct to update the product
                    val intent = Intent(context, AddProduct::class.java)
                    intent.putExtra("productId", id)
                    intent.putExtra("email", email)
                    context.startActivity(intent)
                }
            }
            // go to Product Page by clicking on the Item
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val product = dataList[position]
                    val intent = Intent(context, CustomerProduct::class.java)
                    intent.putExtra("productId", product.Id)
                    intent.putExtra("sellerEmail", product.sellerEmail)
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
        holder.email = Product.sellerEmail

        // get all images of the product
        val img = db2.getAllProductImages(holder.id, holder.email)
        Picasso.get()
            .load(img[0])
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
