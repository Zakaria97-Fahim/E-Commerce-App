package com.example.ecomapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ProductsCart_DB(context: Context) : SQLiteOpenHelper(context, "Cart_Table", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE Cart_Table (Id INTEGER PRIMARY KEY AUTOINCREMENT, SellerEmail TEXT , IdProduct INTEGER , UserEmail TEXT )")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Cart_Table")
        onCreate(db)
    }
    // Add Product to Cart
    fun addToCart(product: ProductsCart): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("sellerEmail", product.sellerEmail)
        values.put("IdProduct", product.id)
        values.put("UserEmail", product.userEmail)

        return db.insert("Cart_Table", null, values)
    }

    // Delete Product from the Cart
    fun deleteFromCart(id: Int, sellerEmail: String): Boolean {
        val db = this.readableDatabase
        val query = "DELETE FROM Cart_Table WHERE IdProduct = ? AND sellerEmail = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString() , sellerEmail))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // Delete Product
    fun deleteId(id: Int, sellerEmail: String, userEmail: String): Boolean {
        val db = this.readableDatabase
        val query = "DELETE FROM Cart_Table WHERE IdProduct = ? AND sellerEmail = ? AND userEmail = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString() , sellerEmail, userEmail))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // Check if Email exists
    fun checkIfProductExists(clientEmail: String, idProduct: Int): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM Cart_Table WHERE UserEmail = ? AND IdProduct = ?"
        val cursor = db.rawQuery(query, arrayOf(clientEmail, idProduct.toString()))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun getAllCartProducts(): ArrayList<ProductsCart> {
        val IdsList = ArrayList<ProductsCart>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Cart_Table", null)
        if (cursor.moveToFirst()) {
            do {
                val sellerEmail = cursor.getString(1)
                val id = cursor.getInt(2)
                val userEmail = cursor.getString(3)
                val ids = ProductsCart(sellerEmail, id, userEmail)
                IdsList.add(ids)
            } while (cursor.moveToNext())
        }
        return IdsList
    }
}
