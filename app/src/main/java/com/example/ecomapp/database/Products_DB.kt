package com.example.ecomapp.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Products_DB(context : Context ) : SQLiteOpenHelper(context,"Products_table",null,1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE Products_table ( Email TEXT , Id INTEGER PRIMARY KEY, Name TEXT, Description TEXT," +
                " Category TEXT, Brand TEXT, Price DOUBLE, Quantity INTEGER,QAcheter INTEGER,QRest INTEGER, Weight DOUBLE, " +
                "Dimension DOUBLE, Sh_Cost DOUBLE,  Stock_Level INTEGER, Min_Threshold INTEGER, images TEXT, Remise INTEGER, Review DOUBLE, etats TEXT)")
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Products_table")
        onCreate(db)
    }

// Add New User ------------------------------------------------------------------------------------
    fun add_Product(product : Product):Long {

        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Email", product.Email)
        values.put("Id", product.Id)
        values.put("Name", product.Name)
        values.put("Description", product.Description)
        values.put("Category", product.Category)
        values.put("Brand", product.Brand)
        values.put("Price", product.Price)
        values.put("Quantity", product.Quantity)
        values.put("QAcheter", product.QAcheter)
        values.put("QRest", product.QRest)
        values.put("Weight", product.Weight)
        values.put("Dimension", product.dimension)
        values.put("Sh_Cost", product.Sh_Cost)
        values.put("Stock_Level", product.Stock_Level)
        values.put("Min_Threshold ", product.Min_Threshold)
        values.put("images", product.images)
        values.put("Remise", product.remise)
        values.put("Review", product.review)
        values.put("etats", product.etats)

    // Insert the new record
        return db.insert("Products_table", null, values)
    }

    // update Product-------------------------------------------------------------------------------
    fun update(product: Product, id: Int, email: String) {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put("Name", product.Name)
        values.put("Description", product.Description)
        values.put("Category", product.Category)
        values.put("Brand", product.Brand)
        values.put("Price", product.Price)
        values.put("Quantity", product.Quantity)
        values.put("QAcheter", product.QAcheter)
        values.put("QRest", product.QRest)
        values.put("Weight", product.Weight)
        values.put("Dimension", product.dimension)
        values.put("Sh_Cost", product.Sh_Cost)
        values.put("Stock_Level", product.Stock_Level)
        values.put("Min_Threshold ", product.Min_Threshold)
        values.put("images", product.images)
        values.put("Remise", product.remise)
        values.put("Review", product.review)
        values.put("etats", product.etats)

        // Define the WHERE clause to specify which row(s) to update
        val whereClause = "Id = ? AND Email = ?"
        val whereArgs = arrayOf(id.toString(), email)

        // Execute the update query
        db.update("Products_table", values, whereClause, whereArgs)
        db.close()
    }

    // Update Etats  --------------------------------------------------------------------------
    fun modify(id: Int, email: String, newEtats: String) {
        val db = this.writableDatabase
        val query = "UPDATE Products_table SET etats = ? WHERE Id = ? AND Email = ?"
        db.execSQL(query, arrayOf(newEtats, id.toString(), email))
    }
    // Update Quantity  --------------------------------------------------------------------------

    fun modifyQuantity(id: Int, email: String, qacheter: Int) {
        val db = this.writableDatabase
        val query = "UPDATE Products_table SET qAcheter = qAcheter + ?, QRest = QRest - ? WHERE Id = ? AND Email = ?"
        db.execSQL(query, arrayOf(qacheter, qacheter, id.toString(), email))
    }


    // Update Reviews  --------------------------------------------------------------------------
    fun modifyReviews(id: Int, email: String, reviews: Double) {
        val db = this.writableDatabase
        val query = "UPDATE Products_table SET Review = ? WHERE Id = ? AND Email = ?"
        db.execSQL(query, arrayOf(reviews, id.toString(), email))
    }
    // Check if Id exists --------------------------------------------------------------------------
    fun checkIf_ID_Exists(id : Int): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM Products_table WHERE Id = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString()))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    //get number of products -----------------------------------------------------------------------
    fun num_of_row(): Int {
        val db = this.readableDatabase
        val query = "SELECT COUNT(*) FROM Products_table "
        val cursor = db.rawQuery(query, null)
        var rows_num = 0
        if (cursor.moveToFirst()) {
            rows_num = cursor.getInt(0) // Get the count from the first column
        }
        cursor.close()
        return rows_num
    }

    //Delete Product ----------------------------------------------------------------------------------
    fun delete(id: Int, email: String): Boolean {
        val db = this.readableDatabase
        val query = "DELETE FROM Products_table WHERE Id = ? AND Email = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString() , email))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    //get seller Products  ----------------------------------------------------------------------------------
    fun getAll_S(email: String): ArrayList<Product> {

        val db = this.readableDatabase
        val query = "SELECT * FROM Products_table WHERE Email = ?"
        val cursor = db.rawQuery(query, arrayOf(email))

        return products(cursor)

    }
    // get All Products --------------------------------------------------------------------------
    fun getAll():ArrayList<Product> {

        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Products_table", null)
        return products(cursor)
    }
//--------------------------------------------------------------------------------------------------
    @SuppressLint("Range")
    fun products(cursor: Cursor):ArrayList<Product>{
        val productList = ArrayList<Product>()
        while (cursor.moveToNext()) {
            val product = Product(
                cursor.getString(cursor.getColumnIndex("Email")),
                cursor.getInt(cursor.getColumnIndex("Id")),
                cursor.getString(cursor.getColumnIndex("Name")),
                cursor.getString(cursor.getColumnIndex("Description")),
                cursor.getString(cursor.getColumnIndex("Category")),
                cursor.getString(cursor.getColumnIndex("Brand")),
                cursor.getDouble(cursor.getColumnIndex("Price")),
                cursor.getInt(cursor.getColumnIndex("Quantity")),
                cursor.getInt(cursor.getColumnIndex("QAcheter")),
                cursor.getInt(cursor.getColumnIndex("QRest")),
                cursor.getDouble(cursor.getColumnIndex("Weight")),
                cursor.getDouble(cursor.getColumnIndex("Dimension")),
                cursor.getDouble(cursor.getColumnIndex("Sh_Cost")),
                cursor.getInt(cursor.getColumnIndex("Stock_Level")),
                cursor.getInt(cursor.getColumnIndex("Min_Threshold")),
                cursor.getString(cursor.getColumnIndex("images")),
                cursor.getInt(cursor.getColumnIndex("Remise")),
                cursor.getDouble(cursor.getColumnIndex("Review")),
                cursor.getString(cursor.getColumnIndex("etats"))
                )
            productList.add(product)
        }
        cursor.close()
        return productList
    }

}

