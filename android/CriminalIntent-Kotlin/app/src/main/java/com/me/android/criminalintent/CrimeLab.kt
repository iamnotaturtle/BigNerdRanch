package com.me.android.criminalintent

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.me.android.criminalintent.database.CrimeBaseHelper
import com.me.android.criminalintent.database.CrimeCursorWrapper
import com.me.android.criminalintent.database.CrimeDbSchema.CrimeTable
import java.util.*

class CrimeLab(context: Context) {
    val crimes = arrayListOf<Crime>()

    private val database: SQLiteDatabase = CrimeBaseHelper(context).writableDatabase

    fun getCrime(id: UUID): Crime? {
        val wrapper = queryCrimes("${CrimeTable.Cols.UUID} = ?", arrayOf(id.toString()))

        wrapper.use { cursor ->

            if (cursor.count == 0) {
                return null
            }

            cursor.moveToFirst()
            return cursor.getCrime()
        }
    }

    fun getCrimes(): List<Crime> {
        crimes.clear()
        val wrapper = queryCrimes(null, null)

        wrapper.use { cursor ->
            cursor.moveToFirst()
            while(!cursor.isAfterLast) {
                crimes.add(cursor.getCrime())
                cursor.moveToNext()
            }
            cursor.close()
        }
        return crimes
    }

    fun addCrime(crime: Crime) {
        val values = getContentValues(crime)
        database.insert(CrimeTable.NAME, null, values)
    }

    fun updateCrime(crime: Crime) {
        val uuid = crime.id.toString()
        val values = getContentValues(crime)
        database.update(CrimeTable.NAME, values, "${CrimeTable.Cols.UUID} = ?", arrayOf(uuid))
    }

    private fun queryCrimes(whereClause: String?, whereArgs: Array<String>?): CrimeCursorWrapper {
        val cursor = database.query(CrimeTable.NAME, null, whereClause, whereArgs, null, null, null)
        return CrimeCursorWrapper(cursor)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile private var INSTANCE: CrimeLab? = null

        fun getInstance(context: Context?): CrimeLab =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: CrimeLab(context!!).also { INSTANCE = it }
            }

        private fun getContentValues(crime: Crime): ContentValues {
            val values = ContentValues()
            values.put(CrimeTable.Cols.UUID, crime.id.toString())
            values.put(CrimeTable.Cols.TITLE, crime.title)
            values.put(CrimeTable.Cols.DATE, crime.date.time)
            values.put(CrimeTable.Cols.SOLVED, if (crime.isSolved) 1 else 0)
            return values
        }

    }
}