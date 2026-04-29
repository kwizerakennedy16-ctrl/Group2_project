package com.ndejje.campusconnect.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey

/**
 * Represents a campus announcement pushed by administration, lecturers, or the student guild.
 */
@Entity(tableName = "announcements")
data class Announcement(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val body: String,
    val postedBy: String,
    val category: AnnouncementCategory,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false // Default value, will be overridden by DAO join queries
)

/**
 * Tracks which announcements have been read by which user.
 */
@Entity(
    tableName = "announcement_read_status",
    primaryKeys = ["announcementId", "userEmail"],
    foreignKeys = [
        ForeignKey(
            entity = Announcement::class,
            parentColumns = ["id"],
            childColumns = ["announcementId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AnnouncementReadStatus(
    val announcementId: Int,
    val userEmail: String
)

enum class AnnouncementCategory {
    ACADEMIC,
    EVENTS,
    EMERGENCY,
    GENERAL
}
