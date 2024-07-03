package com.example.ecomapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Image_DB(context: Context) : SQLiteOpenHelper(context, "DB_Images", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE DB_Images (Email TEXT, Id INTEGER PRIMARY KEY, Img1 TEXT , Img2 TEXT , Img3 TEXT,Img4 TEXT,Img5 TEXT, Img6 TEXT, Img7 TEXT, Img8 TEXT, Img9 TEXT, Img10 TEXT )")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS DB_Images")
        onCreate(db)
    }

    // Add New id
    fun addIMg(img:Images): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Email", img.email)
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
    //Delete Product ----------------------------------------------------------------------------------
    fun delete(id: Int, email: String): Boolean {
        val db = this.readableDatabase
        val query = "DELETE FROM DB_Images WHERE Id = ? AND Email = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString() , email))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
    //Update----------------------------------------------------------------------------------------
    fun upIMg(img: Images, id: Int, email: String) {
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
        val whereClause = "Id = ? AND Email = ?"
        val whereArgs = arrayOf(id.toString(), email)

        // Execute the update query
        db.update("DB_Images", values, whereClause, whereArgs)
        db.close()
    }

    // Check if Email exists
    fun checkIfEmailExists(userEmail: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM DB_Images WHERE UserEmail = ?"
        val cursor = db.rawQuery(query, arrayOf(userEmail))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun getAll_iP(id:Int, email: String): ArrayList<Images> {
        val imageList = ArrayList<Images>()
        val db = this.readableDatabase

        val query = "SELECT * FROM DB_Images WHERE Id = ? AND Email = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString() , email))

        if (cursor.moveToFirst()) {
            do {
                val email = cursor.getString(0)
                val id = cursor.getInt(1)
                val img1 = cursor.getString(2)
                val img2 = cursor.getString(3)
                val img3 = cursor.getString(4)
                val img4 = cursor.getString(5)
                val img5 = cursor.getString(6)
                val img6 = cursor.getString(7)
                val img7 = cursor.getString(8)
                val img8 = cursor.getString(9)
                val img9 = cursor.getString(10)
                val img10 = cursor.getString(11)

                val imges = Images(email,id,img1,img2,img3,img4,img5,img6,img7,img8,img9,img10)
                imageList.add(imges)

            } while (cursor.moveToNext())
        }
        return imageList
    }

}
