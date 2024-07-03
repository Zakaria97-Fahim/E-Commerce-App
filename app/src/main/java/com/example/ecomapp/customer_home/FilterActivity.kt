package com.example.ecomapp.customer_home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.SeekBar
import com.example.ecomapp.R

class FilterActivity : AppCompatActivity() {
    private lateinit var priceMinEditText: EditText
    private lateinit var priceMaxEditText: EditText
    private lateinit var discount50RadioButton: RadioButton
    private lateinit var discount40RadioButton: RadioButton
    private lateinit var discount30RadioButton: RadioButton
    private lateinit var discount20RadioButton: RadioButton
    private lateinit var discount10RadioButton: RadioButton
    private lateinit var review4RadioButton: RadioButton
    private lateinit var review3RadioButton: RadioButton
    private lateinit var review2RadioButton: RadioButton
    private lateinit var review1RadioButton: RadioButton
    private lateinit var seekbar: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.filter_page)

        // Initialize views
        val backButton: ImageButton = findViewById(R.id.imageButton)
        val clearButton: Button = findViewById(R.id.effacer)
        val saveButton: Button = findViewById(R.id.ENREGISTRER)
        seekbar = findViewById(R.id.range_of_price)
        priceMinEditText = findViewById(R.id.pricePanier)
        priceMaxEditText = findViewById(R.id.price2)
        discount50RadioButton = findViewById(R.id.r50)
        discount40RadioButton = findViewById(R.id.r40)
        discount30RadioButton = findViewById(R.id.r30)
        discount20RadioButton = findViewById(R.id.r20)
        discount10RadioButton = findViewById(R.id.r10)
        review4RadioButton = findViewById(R.id.s4)
        review3RadioButton = findViewById(R.id.s3)
        review2RadioButton = findViewById(R.id.s2)
        review1RadioButton = findViewById(R.id.s1)

        val maxValue = 5000
        seekbar.max = maxValue
        seekbar.min = 0

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // Debugging statement
                Log.d("SeekBarProgress", "Progress: $progress")

                // Ensure priceMinEditText is not null
                priceMinEditText.setText("0")

                // Check if seekBar is not null and update priceMaxEditText
                seekBar?.let {
                    val selectedValue = it.progress
                    priceMaxEditText.setText(selectedValue.toString())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                priceMinEditText.setText("0")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val selectedValue = seekBar?.progress ?: 0
                val clampedValue = if (selectedValue > maxValue) maxValue else selectedValue
                priceMaxEditText.setText(clampedValue.toString())
            }
        })


        // Handle click on the back button to return to the previous page
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Handle click on the clear button to clear all filter options
        clearButton.setOnClickListener {
            clearFilters()
        }

        // Handle click on the save button to apply filters and return to the home page
        saveButton.setOnClickListener {
            applyFilters()
        }
    }

    private fun clearFilters() {
        seekbar.progress = 0
        priceMinEditText.text.clear()
        priceMaxEditText.text.clear()
        discount50RadioButton.isChecked = false
        discount40RadioButton.isChecked = false
        discount30RadioButton.isChecked = false
        discount20RadioButton.isChecked = false
        discount10RadioButton.isChecked = false
        review4RadioButton.isChecked = false
        review3RadioButton.isChecked = false
        review2RadioButton.isChecked = false
        review1RadioButton.isChecked = false
    }

    private fun applyFilters() {
        val minPrice = priceMinEditText.text.toString().toIntOrNull() ?: 0
        val maxPrice = priceMaxEditText.text.toString().toIntOrNull() ?: 50000

        val discount = when {
            discount50RadioButton.isChecked -> 50
            discount40RadioButton.isChecked -> 40
            discount30RadioButton.isChecked -> 30
            discount20RadioButton.isChecked -> 20
            discount10RadioButton.isChecked -> 10
            else -> 0
        }
        val review = when {
            review4RadioButton.isChecked -> 4.0
            review3RadioButton.isChecked -> 3.0
            review2RadioButton.isChecked -> 2.0
            review1RadioButton.isChecked -> 1.0
            else -> 0.0
        }

        val intent = Intent(this, Home::class.java)
        intent.putExtra("fil","Filter")
        intent.putExtra("minPrice", minPrice)
        intent.putExtra("maxPrice", maxPrice)
        intent.putExtra("discount", discount)
        intent.putExtra("review", review)
        startActivity(intent)

    }

}
