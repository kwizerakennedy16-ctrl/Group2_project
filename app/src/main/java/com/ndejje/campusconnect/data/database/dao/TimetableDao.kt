package com.ndejje.campusconnect.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ndejje.campusconnect.data.model.TimetableEntry
import com.ndejje.campusconnect.data.model.TimetableType
import kotlinx.coroutines.flow.Flow

@Dao
interface TimetableDao {

    @Query("SELECT * FROM timetable_entries ORDER BY dayOfWeek, startTime")
    fun getAllEntries(): Flow<List<TimetableEntry>>

    @Query("SELECT * FROM timetable_entries WHERE dayOfWeek = :day ORDER BY startTime")
    fun getEntriesByDay(day: String): Flow<List<TimetableEntry>>

    @Query("SELECT * FROM timetable_entries WHERE entryType = :type ORDER BY dayOfWeek, startTime")
    fun getEntriesByType(type: TimetableType): Flow<List<TimetableEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: TimetableEntry)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<TimetableEntry>)

    @Query("DELETE FROM timetable_entries")
    suspend fun clearAll()
}
