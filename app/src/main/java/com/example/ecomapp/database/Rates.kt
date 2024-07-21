package com.example.ecomapp.database

data class Rates(val id: Int, val rates:Double, val sellerEmail:String, val userEmail: String,
                 val date: String )
// try dateLocal