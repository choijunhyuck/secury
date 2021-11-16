package com.codelog.secury.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

import net.sqlcipher.database.*

class DatabaseHelper(context: Context,
                        factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context,
        DATABASE_NAME,
        factory,
        DATABASE_VERSION
    ) {

    override fun onCreate(db: SQLiteDatabase) {

        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"  +
                COLUMN_TITLE + " TEXT,"+
                COLUMN_URL + " TEXT"+ ")")
        db.execSQL(CREATE_PRODUCTS_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addLink(context: Context, password: String, title: String, url: String) {

        val values = ContentValues()
        values.put(COLUMN_TITLE, title)
        values.put(COLUMN_URL, url)
        val db = loadDB(context, password)
        db.insert(
            TABLE_NAME,
            null,
            values
        );

        db.close()
    }

    fun deleteLink(id:Int, context: Context, password: String) {

        val query =
            "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = \"$id\""

        val db = loadDB(context, password)

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val rowId = Integer.parseInt(cursor.getString(0))
            db.delete(
                TABLE_NAME, COLUMN_ID + " = ?",
                arrayOf(rowId.toString()))
            cursor.close()
        }

        db.close()

    }

    fun getAllLinks(context: Context, password: String): Cursor? {
        val db = loadDB(context, password)
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun deleteAllLinks(context: Context, password: String){
        val db = loadDB(context,password)
        db.execSQL("DELETE FROM "+ TABLE_NAME)
        db.close()
    }

    fun loadDB(context: Context, password: String):SQLiteDatabase{
        val databaseFile = context.getDatabasePath("secury.db")
        val database = SQLiteDatabase.openOrCreateDatabase(databaseFile, password,null)
        return database
    }

    companion object {

        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "secury_database.db"
        val TABLE_NAME = "links"
        val COLUMN_ID = "_id"
        val COLUMN_TITLE = "title"
        val COLUMN_URL = "url"

    }
}