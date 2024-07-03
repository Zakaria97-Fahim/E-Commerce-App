package com.example.ecomapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PanierProducts_DB(context: Context) : SQLiteOpenHelper(context, "Panier_Table", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE Panier_Table (sellerEmail TEXT , Id INTEGER PRIMARY KEY, UserEmail TEXT )")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Panier_Table")
        onCreate(db)
    }

    // Add New id
    fun ajouter(ids: Panier_Products): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("sellerEmail", ids.sellerEmail)
        values.put("Id", ids.id)
        values.put("UserEmail", ids.userEmail)

        return db.insert("Panier_Table", null, values)
    }
    //Delete Product ----------------------------------------------------------------------------------
    fun delete(id: Int, sellerEmail: String): Boolean {
        val db = this.readableDatabase
        val query = "DELETE FROM Panier_Table WHERE Id = ? AND sellerEmail = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString() , sellerEmail))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
    //Delete Product ----------------------------------------------------------------------------------
    fun deleteId(id: Int, sellerEmail: String, userEmail: String): Boolean {
        val db = this.readableDatabase
        val query = "DELETE FROM Panier_Table WHERE Id = ? AND sellerEmail = ? AND userEmail = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString() , sellerEmail, userEmail))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }
    // Check if Email exists
    fun checkIfEmailExists(userEmail: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM Panier_Table WHERE UserEmail = ?"
        val cursor = db.rawQuery(query, arrayOf(userEmail))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun getAll(): ArrayList<Panier_Products> {
        val IdsList = ArrayList<Panier_Products>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Panier_Table", null)
        if (cursor.moveToFirst()) {
            do {
                val sellerEmail = cursor.getString(0)
                val id = cursor.getInt(1)
                val userEmail = cursor.getString(2)
                val ids = Panier_Products(sellerEmail, id, userEmail)
                IdsList.add(ids)
            } while (cursor.moveToNext())
        }
        return IdsList
    }
}
