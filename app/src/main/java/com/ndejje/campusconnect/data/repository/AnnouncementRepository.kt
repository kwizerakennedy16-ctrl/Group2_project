package com.ndejje.campusconnect.data.repository

import com.ndejje.campusconnect.data.database.dao.AnnouncementDao
import com.ndejje.campusconnect.data.model.Announcement
import com.ndejje.campusconnect.data.model.AnnouncementCategory
import com.ndejje.campusconnect.data.model.AnnouncementReadStatus
import kotlinx.coroutines.flow.Flow

/**
 * Single source of truth for announcement data.
 * Abstracts the DAO from the ViewModel layer.
 */
class AnnouncementRepository(private val announcementDao: AnnouncementDao) {

    fun getAllAnnouncements(userEmail: String): Flow<List<Announcement>> =
        announcementDao.getAllAnnouncements(userEmail)

    fun getAnnouncementsByCategory(category: AnnouncementCategory, userEmail: String): Flow<List<Announcement>> =
        announcementDao.getAnnouncementsByCategory(category.name, userEmail)

    fun getUnreadCount(userEmail: String): Flow<Int> = announcementDao.getUnreadCount(userEmail)

    suspend fun insert(announcement: Announcement) =
        announcementDao.insertAnnouncement(announcement)

    suspend fun markAsRead(announcementId: Int, userEmail: String) =
        announcementDao.markAsRead(AnnouncementReadStatus(announcementId, userEmail))

    suspend fun deleteAnnouncement(announcementId: Int) =
        announcementDao.deleteAnnouncement(announcementId)
}
