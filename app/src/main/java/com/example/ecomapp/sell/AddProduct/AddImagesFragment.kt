package com.example.ecomapp.sell.AddProduct

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.ecomapp.R

class AddImagesFragment : Fragment() {


    // Displaying the fragment on the screen, and get the Interactions
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val v     = inflater.inflate(R.layout.fragment_to_add_image, container, false)
        val add   = v.findViewById<Button>(R.id.upld)
        val close = v.findViewById<Button>(R.id.clse)
        val text  = v.findViewById<EditText>(R.id.url)
        // display the image at RecyclerView
        add.setOnClickListener {
            // get the Url
            val url = text.text.toString()
            // send URL to a AddProduct.kt , to display at RecyclerView
            val result = Bundle().apply { putString("url_key", url) }
            parentFragmentManager.setFragmentResult("requestKey", result)

            Toast.makeText(requireContext(), "URL is added", Toast.LENGTH_SHORT).show()
            // clear text from edittext
            v.findViewById<EditText>(R.id.url).text.clear()
        }
        close.setOnClickListener {
            requireFragmentManager().beginTransaction().remove(this).commit()
        }
        return v
    }
}