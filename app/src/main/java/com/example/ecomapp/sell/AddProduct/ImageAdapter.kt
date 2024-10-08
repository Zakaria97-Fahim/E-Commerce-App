package com.example.ecomapp.sell.AddProduct

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.ecomapp.R
import com.squareup.picasso.Picasso

class ImageAdapter(private val dataList: MutableList<String>) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imag   : ImageView = itemView.findViewById(R.id.imageView2)
        val delete : Button    = itemView.findViewById(R.id.delimg)
        init {
            // Delete the item (Image)
            delete.setOnClickListener {
                val position = adapterPosition
                if (position!=RecyclerView.NO_POSITION){
                    removeItem(position)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val image = dataList[position]

        Picasso.get()
            .load(image)
            .into(holder.imag)
    }

    fun removeItem(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
