package com.example.ecomapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Rate_DB(context: Context) : SQLiteOpenHelper(context, "Rates_Table", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE Rates_Table (Rate DOUBLE, sellerEmail TEXT , Id INTEGER , " +
                "UserEmail TEXT, dateR TEXT )")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Rates_Table")
        onCreate(db)
    }

    // Add New Rate
    fun addRate(rate: Rates): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Rate", rate.rates)
        values.put("sellerEmail", rate.sellerEmail)
        values.put("Id", rate.id)
        values.put("UserEmail", rate.userEmail)
        values.put("dateR", rate.date)

        return db.insert("Rates_Table", null, values)
    }
    // Update Rate
    fun updateRate(id: Int, seller: String, user: String, rate: Double) {
        val db = this.writableDatabase
        val query = "UPDATE Rates_Table SET Rate = ? WHERE Id = ? AND sellerEmail = ?AND userEmail = ?"
        db.execSQL(query, arrayOf(rate, id.toString(), seller, user))
    }
    // Delete the Rate
    fun deleteRate(id: Int, sellerEmail: String, userEmail: String): Boolean {
        val db = this.readableDatabase
        val query = "DELETE FROM Rates_Table WHERE Id = ? AND sellerEmail = ? AND userEmail = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString() , sellerEmail, userEmail))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // Get All Rates
    fun getAllRates(): ArrayList<Rates> {
        val rList = ArrayList<Rates>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Rates_Table", null)
        if (cursor.moveToFirst()) {
            do {
                val rate = cursor.getDouble(0)
                val sellerEmail = cursor.getString(1)
                val id = cursor.getInt(2)
                val userEmail = cursor.getString(3)
                val date = cursor.getString(4)

                val rates = Rates(id, rate, sellerEmail, userEmail, date)
                rList.add(rates)
            } while (cursor.moveToNext())
        }
        return rList
    }

}
