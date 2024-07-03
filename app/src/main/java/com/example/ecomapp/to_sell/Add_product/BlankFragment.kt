package com.example.ecomapp.to_sell.Add_product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.ecomapp.R
import com.example.zms.to_sell.Add_product.Url_new

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BlankFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BlankFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_blank, container, false)
        val add = v.findViewById<Button>(R.id.upld)
        val close = v.findViewById<Button>(R.id.clse)

        val newurl : Url_new = activity as Url_new
        add.setOnClickListener {
            val url = v.findViewById<EditText>(R.id.url).text.toString()
            newurl.getUrl(url)
            Toast.makeText(requireContext(), "url is added", Toast.LENGTH_SHORT).show()
            v.findViewById<EditText>(R.id.url).text.clear()
        }
        close.setOnClickListener {
            requireFragmentManager().beginTransaction().remove(this).commit()
        }

        return v
    }
}