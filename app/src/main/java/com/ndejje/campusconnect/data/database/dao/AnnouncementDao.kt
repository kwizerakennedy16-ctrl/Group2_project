package com.ndejje.campusconnect.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.ndejje.campusconnect.data.model.Announcement
import com.ndejje.campusconnect.data.model.AnnouncementReadStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface AnnouncementDao {

    @Query("""
        SELECT a.*, 
        CASE WHEN r.userEmail IS NOT NULL THEN 1 ELSE 0 END as isRead 
        FROM announcements a 
        LEFT JOIN announcement_read_status r ON a.id = r.announcementId AND r.userEmail = :userEmail
        ORDER BY a.timestamp DESC
    """)
    fun getAllAnnouncements(userEmail: String): Flow<List<Announcement>>

    @Query("""
        SELECT a.*, 
        CASE WHEN r.userEmail IS NOT NULL THEN 1 ELSE 0 END as isRead 
        FROM announcements a 
        LEFT JOIN announcement_read_status r ON a.id = r.announcementId AND r.userEmail = :userEmail
        WHERE a.category = :category 
        ORDER BY a.timestamp DESC
    """)
    fun getAnnouncementsByCategory(category: String, userEmail: String): Flow<List<Announcement>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: Announcement)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(announcements: List<Announcement>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun markAsRead(readStatus: AnnouncementReadStatus)

    @Query("""
        SELECT COUNT(*) FROM announcements a
        WHERE a.id NOT IN (
            SELECT announcementId FROM announcement_read_status WHERE userEmail = :userEmail
        )
    """)
    fun getUnreadCount(userEmail: String): Flow<Int>

    @Query("DELETE FROM announcements WHERE id = :announcementId")
    suspend fun deleteAnnouncement(announcementId: Int)

    @Query("DELETE FROM announcements")
    suspend fun clearAll()
}
