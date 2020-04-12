package com.me.android.criminalintent

import java.util.*

class Crime(_id: UUID = UUID.randomUUID()) {
    var id: UUID  = _id
    var date: Date = Date()
    var title: String? = null
    var isSolved: Boolean = false
}