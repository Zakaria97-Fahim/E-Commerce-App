package com.example.ecomapp.inscrip

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.ecomapp.MainActivity
import com.example.ecomapp.R
import com.example.ecomapp.database.User_DB
import kotlin.random.Random
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlinx.coroutines.*

class ForgottenPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgotten_password)

        val userEmail: EditText = findViewById(R.id.emailF)
        val sendButton: Button = findViewById(R.id.send)
        val userDB = User_DB(applicationContext)
        sendButton.setOnClickListener {
            val email = userEmail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            } else {
                if (userDB.checkIfEmailExists(email)){
                    val code = generateSixDigitCode()
                    sendEmail(this, email, code)
                    val i = Intent(this, CodeConfirmation::class.java)
                    i.putExtra("code", code)
                    i.putExtra("userMail", email)
                    startActivity(i)
                }else{
                    Toast.makeText(this, "your email is not Exists !", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }


    fun generateSixDigitCode(): String {
        val code = Random.nextInt(100000, 999999)
        return code.toString()
    }

    fun sendEmail(context: Context, toEmail: String, code: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val properties = Properties().apply {
                put("mail.smtp.auth", "true")
                put("mail.smtp.starttls.enable", "true")
                put("mail.smtp.host", "smtp.gmail.com")
                put("mail.smtp.port", "587")
                put("mail.smtp.ssl.trust", "smtp.gmail.com")
                put("mail.smtp.connectiontimeout", "10000") // 10 seconds
                put("mail.smtp.timeout", "10000") // 10 seconds
                put("mail.smtp.writetimeout", "10000") // 10 seconds
            }
            val session = Session.getInstance(properties, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication("zakariafahim61@gmail.com", "tntz flor tcsw qloq")
                }
            })
            try {
                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress("zakariafahim61@gmail.com"))
                    setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail))
                    subject = "Password Reset Code"
                    setText("Your password reset code is: $code")
                }
                Transport.send(message)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "The code was sent", Toast.LENGTH_SHORT).show()
                }
            } catch (e: MessagingException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Failed to send email: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
