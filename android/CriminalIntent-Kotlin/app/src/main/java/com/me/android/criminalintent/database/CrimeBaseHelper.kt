package com.me.android.criminalintent.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

private const val VERSION = 1
private const val DATABASE_NAME = "crime-Base.db"

class CrimeBaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            CREATE TABLE ${CrimeDbSchema.CrimeTable.NAME} (
                _id INTEGER PRIMARY KEY AUTOINCREMENT,
                ${CrimeDbSchema.CrimeTable.Cols.UUID},
                ${CrimeDbSchema.CrimeTable.Cols.TITLE},
                ${CrimeDbSchema.CrimeTable.Cols.DATE},
                ${CrimeDbSchema.CrimeTable.Cols.SOLVED}
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}