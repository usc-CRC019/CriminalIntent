package com.bignerdranch.android.criminalintent

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Workout(@PrimaryKey val id: UUID = UUID.randomUUID(),
                   var title: String = "",
                   var date: Date = Date(),
                   var isGrouped: Boolean = false,
                   var startTime: String = "Tap Here For Start Time",
                   var endTime: String = "Tap Here For End Time",
                   var place: String = "")