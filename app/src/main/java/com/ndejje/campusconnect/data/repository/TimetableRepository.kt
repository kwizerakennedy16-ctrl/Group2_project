package com.ndejje.campusconnect.data.repository

import com.ndejje.campusconnect.data.database.dao.TimetableDao
import com.ndejje.campusconnect.data.model.TimetableEntry
import com.ndejje.campusconnect.data.model.TimetableType
import kotlinx.coroutines.flow.Flow

/**
 * Single source of truth for academic schedule data.
 */
class TimetableRepository(private val timetableDao: TimetableDao) {

    fun getAllEntries(): Flow<List<TimetableEntry>> =
        timetableDao.getAllEntries()

    fun getEntriesByDay(day: String): Flow<List<TimetableEntry>> =
        timetableDao.getEntriesByDay(day)

    fun getLectures(): Flow<List<TimetableEntry>> =
        timetableDao.getEntriesByType(TimetableType.LECTURE)

    fun getExams(): Flow<List<TimetableEntry>> =
        timetableDao.getEntriesByType(TimetableType.EXAM)

    fun getDeadlines(): Flow<List<TimetableEntry>> =
        timetableDao.getEntriesByType(TimetableType.DEADLINE)

    suspend fun insert(entry: TimetableEntry) =
        timetableDao.insertEntry(entry)
}
