package com.me.android.criminalintent.database

import android.database.Cursor
import android.database.CursorWrapper
import com.me.android.criminalintent.Crime
import com.me.android.criminalintent.database.CrimeDbSchema.CrimeTable
import java.util.*

class CrimeCursorWrapper(cursor: Cursor): CursorWrapper(cursor) {
    fun getCrime(): Crime {
        val uuid = getString(getColumnIndex(CrimeTable.Cols.UUID))
        val title = getString(getColumnIndex(CrimeTable.Cols.TITLE))
        val date = getLong(getColumnIndex(CrimeTable.Cols.DATE))
        val isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED))

        val crime = Crime(UUID.fromString(uuid))
        crime.title = title
        crime.date = Date(date)
        crime.isSolved = isSolved != 0

        return crime
    }
}