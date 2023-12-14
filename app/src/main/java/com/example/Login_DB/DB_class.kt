package com.example.Login_DB
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
class DB_class(context: Context): SQLiteOpenHelper(context,DATABASE_NAME,null,DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "LoginDatabase"
        private val TABLE_CONTACTS = "user"
        private val KEY_NAME = "name"
        private val KEY_UNAME = "username"
        private val KEY_PSWD = "password"
        private val KEY_LASTNAME = "lastname"
        private val KEY_EMAIL = "email"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val newtb = ("CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_NAME + " TEXT," + KEY_UNAME + " TEXT,"
                + KEY_LASTNAME + " TEXT," + KEY_EMAIL + " TEXT,"
                + KEY_PSWD + " TEXT" + ")")
        db?.execSQL(newtb)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS)
        onCreate(db)
    }

    // Inside DB_class
    fun getFirstName(username: String): String {
        val db = this.readableDatabase
        val query = "SELECT $KEY_NAME FROM $TABLE_CONTACTS WHERE $KEY_UNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))

        var firstName = ""
        if (cursor.moveToFirst()) {
            firstName = cursor.getString(cursor.getColumnIndex(KEY_NAME))
        }

        cursor.close()
        db.close()
        return firstName
    }

}