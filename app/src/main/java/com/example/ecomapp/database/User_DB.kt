package com.example.ecomapp.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class User_DB(context : Context ) : SQLiteOpenHelper(context,"DB_Users",null,1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE DB_Users (Nom TEXT, Prenom TEXT, Email TEXT PRIMARY KEY, Password TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS DB_Users")
        onCreate(db)
    }

    // Add New User
    fun addUser(user : User):Long {

        val db = this.writableDatabase
        val values = ContentValues()
        values.put("Nom", user.nom)
        values.put("Prenom", user.prenom)
        values.put("Email", user.email)
        values.put("Password", user.password)

        return db.insert("DB_Users", null, values)
    }

    // Check if Email exists ?
    fun checkIfEmailExists(email: String): Boolean {

        val db = this.readableDatabase
        val query = "SELECT * FROM DB_Users WHERE Email = ?"
        val cursor = db.rawQuery(query, arrayOf(email))
        val exists = cursor.count > 0
        cursor.close()

        return exists
    }

    // Check if Passwrod exists ?
    fun checkIfPasswrdExists(email: String, password: String): Boolean {

        val db = this.readableDatabase
        val query = "SELECT * FROM DB_Users WHERE Email = ?  AND Password = ?"
        val cursor = db.rawQuery(query, arrayOf(email,password))
        val exists = cursor.count > 0
        cursor.close()

        return exists
    }

    // We dont use it yet
    fun getAllUser():ArrayList<User> {

        val UsersList = ArrayList<User>()

        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM DB_Users", null)

        if (cursor.moveToFirst()) {
            do {
                val nom = cursor.getString(0)
                val prenom = cursor.getString(1)
                val email = cursor.getString(2)
                val password = cursor.getString(3)

                val user = User(nom,prenom,email,password)

                UsersList.add(user)

            } while (cursor.moveToNext())
        }
        return UsersList
    }
}