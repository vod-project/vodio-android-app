package app.vodio.com.vodio.services

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getString
import app.vodio.com.vodio.beans.User

class UserLocalDatabase constructor(val context : Context){
    private companion object {
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "localusr.db"
    }

    private object UserLocal{
        object Entry{
            const val TABLE_NAME = "localusr"
            const val COLUMN_NAME_LOGIN = "LOGIN"
            const val COLUMN_NAME_PASSWORD = "PASSWORD"
            const val COLUMN_NAME_IS_CONNECTED = "IS_CONNECTED"
        }
    }
    private val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${UserLocal.Entry.TABLE_NAME} ("+
                    "${UserLocal.Entry.COLUMN_NAME_LOGIN} TEXT PRIMARY KEY, "+
                    "${UserLocal.Entry.COLUMN_NAME_PASSWORD} TEXT," +
                    "${UserLocal.Entry.COLUMN_NAME_IS_CONNECTED} INTEGER)"
    private val SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS ${UserLocal.Entry.TABLE_NAME}"

    private inner class LocalUserDbHelper(context: Context) : SQLiteOpenHelper(context, UserLocalDatabase.DATABASE_NAME, null, UserLocalDatabase.DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL(SQL_DELETE_ENTRIES)
            onCreate(db)
        }

        override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            onUpgrade(db, oldVersion, newVersion)
        }
    }

    fun storeCurrentUser(login : String, password : String){
        val dbHelper = LocalUserDbHelper(context)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(UserLocal.Entry.COLUMN_NAME_LOGIN, login)
            put(UserLocal.Entry.COLUMN_NAME_PASSWORD, password)
            put(UserLocal.Entry.COLUMN_NAME_IS_CONNECTED, 1)
        }
        try{
            db.insertOrThrow(UserLocal.Entry.TABLE_NAME, null, values)
        }catch(e : SQLiteConstraintException){
            val values = ContentValues().apply {
                put(UserLocal.Entry.COLUMN_NAME_PASSWORD, password)
                put(UserLocal.Entry.COLUMN_NAME_IS_CONNECTED, 1)
            }
            db.update(UserLocal.Entry.TABLE_NAME,values,"${UserLocal.Entry.COLUMN_NAME_LOGIN} = ?", arrayOf("${login}"))
        }
        db.close()

    }

    fun updatePasswordUser(login : String, password: String){
        val dbHelper = LocalUserDbHelper(context)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(UserLocal.Entry.COLUMN_NAME_PASSWORD, password)
        }

        db.update(UserLocal.Entry.TABLE_NAME,values,"${UserLocal.Entry.COLUMN_NAME_LOGIN} = ?", arrayOf("${login}"))
        db.close()
    }

    fun getLocalUser() : User?{
        var usr : User? = null
        val dbHelper = LocalUserDbHelper(context)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(UserLocal.Entry.COLUMN_NAME_LOGIN, UserLocal.Entry.COLUMN_NAME_PASSWORD)

        val cursor = db.query(
               UserLocal.Entry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                "${UserLocal.Entry.COLUMN_NAME_IS_CONNECTED} = ?",              // The columns for the WHERE clause
                arrayOf("${1}"),          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null            // The sort order
        )
        if(cursor.moveToNext()){
            usr = User()
            usr.login = cursor.getString(UserLocal.Entry.COLUMN_NAME_LOGIN)
            usr.password = cursor.getString(UserLocal.Entry.COLUMN_NAME_PASSWORD)
        }
        cursor.close()
        db.close()
        return usr
    }

    fun deleteUser(login : String){
        val dbHelper = LocalUserDbHelper(context)
        val db = dbHelper.writableDatabase
        db.delete(UserLocal.Entry.TABLE_NAME,"login = ?", arrayOf(login))
        db.close()
    }

    fun disconnectUser(login: String){
        val dbHelper = LocalUserDbHelper(context)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(UserLocal.Entry.COLUMN_NAME_IS_CONNECTED, 0)
            put(UserLocal.Entry.COLUMN_NAME_PASSWORD, "")
        }

        db.update(UserLocal.Entry.TABLE_NAME,values,"${UserLocal.Entry.COLUMN_NAME_LOGIN} = ?", arrayOf("${login}"))
        db.close()
    }

}