package com.example.ecomapp.inscrip

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.ecomapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CodeConfirmation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.code_confirmation)

        var code = intent.getStringExtra("code")
        val userEmail:String? = intent.getStringExtra("userMail")

        val uemail  : TextView = findViewById(R.id.uEmail)
        val resend  : TextView = findViewById(R.id.resend)
        val count   : TextView = findViewById(R.id.count)

        val confirm : Button   = findViewById(R.id.confirmation)

        val n1    : EditText = findViewById(R.id.n1)
        val n2    : EditText = findViewById(R.id.n2)
        val n3    : EditText = findViewById(R.id.n3)
        val n4    : EditText = findViewById(R.id.n4)
        val n5    : EditText = findViewById(R.id.n5)
        val n6    : EditText = findViewById(R.id.n6)
        //
        focusToEmptyEditText(n1, n2)
        focusToEmptyEditText(n2, n3)
        focusToEmptyEditText(n3, n4)
        focusToEmptyEditText(n4, n5)
        focusToEmptyEditText(n5, n6)
        focusToEmptyEditText(n6, null)

        uemail.text = userEmail
        // start counting
        startCounting()

        // confirm the entry code
        confirm.setOnClickListener {
            // code written by user
            val c = n1.text.toString().trim() + n2.text.toString().trim() +
                    n3.text.toString().trim() + n4.text.toString().trim() +
                    n5.text.toString().trim() + n6.text.toString().trim()

            Toast.makeText(this, c, Toast.LENGTH_SHORT).show()


            // if the code is right open the reset password page
            if(c == code && count.text!="0"){
                // open the Reset Password Page
                val i = Intent(this, ResetPassword::class.java)
                i.putExtra("userMail", userEmail)
                startActivity(i)
            }else {
                Toast.makeText(this, "the code is wrong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
        // resend new code to user email
        resend.setOnClickListener {
            // generate new code
            code = ForgottenPassword().generateSixDigitCode()
            // send it to the userEmail
            if (userEmail != null) {
                ForgottenPassword().sendEmail(this, userEmail, code!!)
            }
            startCounting()
        }

    }

    private fun startCounting() {
        val countTextView: TextView = findViewById(R.id.count)

        CoroutineScope(Dispatchers.Main).launch {
            for (resttime in 60 downTo 0) {
                countTextView.text = resttime.toString()
                delay(1200L) // Wait for 1.2 second
            }
        }
    }
    // focus To Empty EditText, to Enter the next number
    private fun focusToEmptyEditText(currentEditText: EditText, nextEditText: EditText?) {
        currentEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    nextEditText?.requestFocus() // Move focus to the next EditText if it exists
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }


}