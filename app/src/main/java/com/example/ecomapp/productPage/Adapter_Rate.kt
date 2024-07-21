package com.example.ecomapp.productPage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.R
import com.example.ecomapp.database.Rates
import com.example.ecomapp.database.User_DB

class Adapter_Rate(val userE:String, private val context: Context,
                   private val dataList2: ArrayList<Rates>) :
    RecyclerView.Adapter<Adapter_Rate.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_review, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rat: RatingBar = itemView.findViewById(R.id.ratingx)
        val dateR: TextView = itemView.findViewById(R.id.date_Rate)
        val userName: TextView = itemView.findViewById(R.id.userName)

        var user = ""
        var seller = ""
        var idr = 0

        init {
            itemView.setOnClickListener {
                val activity = it!!.context as AppCompatActivity
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION && user == userE) {
                    val fragment = Rate_Fragment.newInstance(idr, seller)
                    activity.supportFragmentManager.beginTransaction()
                        .replace(R.id.containerRate, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val rate = dataList2[position]
        holder.rat.rating = rate.rates.toFloat()
        holder.dateR.text = rate.date
        holder.idr = rate.id
        holder.seller = rate.sellerEmail
        holder.user = rate.userEmail

        val db = User_DB(context)
        val user = db.getAllUser().find { it.email == rate.userEmail }
        val userName = user?.let { "${it.nom} ${it.prenom}" }

        holder.userName.text = "Review by $userName"

    }

    override fun getItemCount(): Int {
        return dataList2.size
    }


}
