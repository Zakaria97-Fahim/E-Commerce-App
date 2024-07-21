package com.example.ecomapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Image_DB(context: Context) : SQLiteOpenHelper(context, "DB_Images", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE DB_Images (SellerEmail TEXT, Id INTEGER PRIMARY KEY , Img1 TEXT , Img2 TEXT , Img3 TEXT,Img4 TEXT,Img5 TEXT, Img6 TEXT, Img7 TEXT, Img8 TEXT, Img9 TEXT, Img10 TEXT )")
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS DB_Images")
        onCreate(db)
    }

    // Add New Product Images
    fun addIMg(img:Images): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("SellerEmail", img.email)
        values.put("Id", img.id)

        values.put("Img1", img.img1)
        values.put("Img2", img.img2)
        values.put("Img3", img.img3)
        values.put("Img4", img.img4)
        values.put("Img5", img.img5)
        values.put("Img6", img.img6)
        values.put("Img7", img.img7)
        values.put("Img8", img.img8)
        values.put("Img9", img.img9)
        values.put("Img10", img.img10)

        return db.insert("DB_Images", null, values)
    }


    // Delete Product Images
    fun deleteIMG(id: Int, email: String): Boolean {
        val db = this.readableDatabase
        val query = "DELETE FROM DB_Images WHERE Id = ? AND SellerEmail = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString() , email))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    //Update Product Images
    fun updateIMg(img: Images, id: Int, email: String) {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put("Img1", img.img1)
        values.put("Img2", img.img2)
        values.put("Img3", img.img3)
        values.put("Img4", img.img4)
        values.put("Img5", img.img5)
        values.put("Img6", img.img6)
        values.put("Img7", img.img7)
        values.put("Img8", img.img8)
        values.put("Img9", img.img9)
        values.put("Img10", img.img10)

        // Define the WHERE clause to specify which row(s) to update
        val whereClause = "Id = ? AND SellerEmail = ?"
        val whereArgs = arrayOf(id.toString(), email)

        // Execute the update query
        db.update("DB_Images", values, whereClause, whereArgs)
        db.close()
    }

    // Check if Email exists ?
    fun checkIfEmailExists(userEmail: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM DB_Images WHERE SellerEmail = ?"
        val cursor = db.rawQuery(query, arrayOf(userEmail))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // get Product images
    fun getAllProductImages(id: Int, email: String): ArrayList<String> {

        var imagesList : ArrayList<String> = arrayListOf()

        val db = this.readableDatabase

        val query = "SELECT * FROM DB_Images WHERE Id = ? AND SellerEmail = ?"

        val cursor = db.rawQuery(query, arrayOf(id.toString(), email))

        // Ensure cursor is not null and has data
        if (cursor != null && cursor.moveToFirst()) {
            val cursorEmail = cursor.getString(cursor.getColumnIndexOrThrow("SellerEmail"))
            val cursorId = cursor.getInt(cursor.getColumnIndexOrThrow("Id"))
            val img1 = cursor.getString(cursor.getColumnIndexOrThrow("Img1"))
            val img2 = cursor.getString(cursor.getColumnIndexOrThrow("Img2"))
            val img3 = cursor.getString(cursor.getColumnIndexOrThrow("Img3"))
            val img4 = cursor.getString(cursor.getColumnIndexOrThrow("Img4"))
            val img5 = cursor.getString(cursor.getColumnIndexOrThrow("Img5"))
            val img6 = cursor.getString(cursor.getColumnIndexOrThrow("Img6"))
            val img7 = cursor.getString(cursor.getColumnIndexOrThrow("Img7"))
            val img8 = cursor.getString(cursor.getColumnIndexOrThrow("Img8"))
            val img9 = cursor.getString(cursor.getColumnIndexOrThrow("Img9"))
            val img10 = cursor.getString(cursor.getColumnIndexOrThrow("Img10"))

            imagesList.addAll(listOf(img1, img2, img3, img4, img5, img6, img7, img8, img9, img10))
        }
        cursor?.close()
        db.close()
        return imagesList
    }
}
