package com.ndejje.campusconnect.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ndejje.campusconnect.viewmodel.UserRole

/**
 * Represents a user of the application.
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val email: String,
    val password: String,
    val name: String,
    val studentId: String,
    val role: UserRole = UserRole.STUDENT
)
