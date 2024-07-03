package com.example.ecomapp.home_pour_visiteur

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.R
import com.squareup.picasso.Picasso

class Adapter_images(private val context: Context, private val dataList: MutableList<String>) :
    RecyclerView.Adapter<Adapter_images.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.img, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagex: ImageView = itemView.findViewById(R.id.imgx)

    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = dataList[position]

        Picasso.get()
            .load(image)
            .into(holder.imagex)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
