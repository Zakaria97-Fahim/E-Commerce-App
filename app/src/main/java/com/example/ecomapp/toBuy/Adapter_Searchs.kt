package com.example.ecomapp.toBuy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat

class Adapter_Searchs(context: Context, private val suggestions: List<String>) :
    ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, suggestions) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false)
        }

        val textView: TextView = view!!.findViewById(android.R.id.text1)
        textView.setTextColor(ContextCompat.getColor(context, android.R.color.black))
        textView.text = suggestions[position]
        return view
    }

}
