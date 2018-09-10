package app.vodio.com.vodio.services

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getStringOrNull
import java.io.File

class VodFilePathLocalDb constructor(val context: Context){
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "DatabaseInfos.db"
    }
    private object DatabaseInfos{
        object Entry{
            const val TABLE_NAME = "vodfilepath"
            const val COLUMN_NAME_ID_VOD = "idVod"
            const val COLUMN_NAME_PATH = "path"
        }
    }
    private  val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${DatabaseInfos.Entry.TABLE_NAME} (" +
                    "${DatabaseInfos.Entry.COLUMN_NAME_ID_VOD} INTEGER PRIMARY KEY, " +
                    "${DatabaseInfos.Entry.COLUMN_NAME_PATH} TEXT)"

    private  val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${DatabaseInfos.Entry.TABLE_NAME}"
    /***/


    private inner class VodFilepathDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }
        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES)
            onCreate(db)
        }
        override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            onUpgrade(db, oldVersion, newVersion)
        }
    }

    fun storeVod(idVod : Int, file : File){
        val dbHelper = VodFilepathDbHelper(context)
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DatabaseInfos.Entry.COLUMN_NAME_ID_VOD, idVod)
            put(DatabaseInfos.Entry.COLUMN_NAME_PATH, file.absolutePath)
        }
        try {
            val newRowId = db?.insertOrThrow(DatabaseInfos.Entry.TABLE_NAME, null, values)
        }catch (e: SQLiteConstraintException){
            // already inserted id
        }
        db.close()
    }

    fun provideFile(idVod : Int) : File?{
        val dbHelper = VodFilepathDbHelper(context)
        val db = dbHelper.readableDatabase

        val projection = arrayOf(DatabaseInfos.Entry.COLUMN_NAME_ID_VOD,DatabaseInfos.Entry.COLUMN_NAME_PATH)

        val selection = "${DatabaseInfos.Entry.COLUMN_NAME_ID_VOD} = ?"
        val selectionArgs = arrayOf("${idVod}")

        val cursor = db.query(
                DatabaseInfos.Entry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null            // The sort order
        )


        if(cursor.moveToNext()) {
            val path = cursor.getStringOrNull(DatabaseInfos.Entry.COLUMN_NAME_PATH)
            if (path != null)
                return File(path)
        }
        cursor.close()
        db.close()
        return null


    }
}