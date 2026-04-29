package com.ndejje.campusconnect.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a single lecture or exam slot in a student's academic schedule.
 */
@Entity(tableName = "timetable_entries")
data class TimetableEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val courseCode: String,
    val courseName: String,
    val lecturer: String,
    val venue: String,
    val dayOfWeek: String,       // e.g. "Monday"
    val startTime: String,       // e.g. "08:00"
    val endTime: String,         // e.g. "10:00"
    val entryType: TimetableType = TimetableType.LECTURE
)

enum class TimetableType {
    LECTURE,
    EXAM,
    DEADLINE
}
