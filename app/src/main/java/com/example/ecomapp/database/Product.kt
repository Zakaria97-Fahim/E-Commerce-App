package com.example.ecomapp.database


data class Product(
    val Email:String, val Id:Int, val Name:String, val Description:String, val Category:String,
    val Brand:String, val Price:Double, val Quantity:Int, val QAcheter:Int, val QRest:Int, val Weight:Double, val dimension:Double,
    val Sh_Cost:Double, val Stock_Level: Int, val Min_Threshold:Int, val images: String,
    val remise: Int, val review: Double, val etats: String
)
