package com.example.ecomapp.productPage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RatingBar
import com.example.ecomapp.R
import com.example.ecomapp.database.Rate_DB
import com.example.ecomapp.database.Rates
import java.time.LocalDate
import kotlin.math.roundToInt

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Rate_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Rate_Fragment : Fragment() {
    private var param1: Int = -1
    private var param2: String? = null
    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: Int, param2: String) =
            Rate_Fragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, containerRate: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val v = inflater.inflate(R.layout.fragment_to_rate, containerRate, false)
        val closeF = v.findViewById<Button>(R.id.delrate)
        val addR =  v.findViewById<Button>(R.id.addrate)


        //RatingBar dynamique-----------------------------------------------------------------------
        val ratingBar = v.findViewById<RatingBar>(R.id.ratingx)
        ratingBar.setOnTouchListener { _, event ->
            val touchX = event.x
            val width = ratingBar.width.toFloat()
            val stars = ratingBar.numStars
            val step = width / stars

            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    val rating = (touchX / width * stars).toFloat()
                    val adjustedRating = (rating / ratingBar.stepSize).roundToInt() * ratingBar.stepSize
                    ratingBar.rating = adjustedRating
                }
                MotionEvent.ACTION_UP -> {
                    // Handle touch release event if needed
                }
            }
            true
        }
        //------------------------------------------------------------------------------------------

        val db = Rate_DB(requireContext())
        val allRates = db.getAllRates()

        val sharedPref = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userEmail = sharedPref.getString("email", null).toString()
        val date = LocalDate.now()

        closeF.setOnClickListener {
            if (ratingBar.rating == 0.0F){
                db.deleteRate(param1, param2.toString(),userEmail)
                val i =Intent(requireContext(),CustomerProduct::class.java)
                i.putExtra("productId",param1)
                i.putExtra("sellerEmail",param2)
                startActivity(i)
            }else{ratingBar.rating = 0.0F }
        }
        addR.setOnClickListener{
            val r = Rates(param1, ratingBar.rating.toDouble(), param2.toString(), userEmail, date.toString())
            for (r in allRates){
                if (r.userEmail==userEmail && r.id==param1 && r.sellerEmail==param2){
                    val rates = ratingBar.rating
                    db.updateRate(param1,param2.toString(),userEmail,rates.toDouble())
                    val i =Intent(requireContext(),CustomerProduct::class.java)
                    i.putExtra("productId",param1)
                    i.putExtra("sellerEmail",param2)
                    startActivity(i)
                    return@setOnClickListener
                }
            }
            db.addRate(r)
            val i =Intent(requireContext(),CustomerProduct::class.java)
            i.putExtra("productId",param1)
            i.putExtra("sellerEmail",param2)
            startActivity(i)
        }
        return v
    }

}