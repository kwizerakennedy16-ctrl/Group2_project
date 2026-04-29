package com.ndejje.campusconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ndejje.campusconnect.data.model.Announcement
import com.ndejje.campusconnect.data.model.AnnouncementCategory
import com.ndejje.campusconnect.data.repository.AnnouncementRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
/**
 * ViewModel responsible for managing announcements and read status tracking.
 *
 * Features:
 * - Fetches announcements from repository using reactive Flow patterns
 * - Supports category filtering (Academic, Events, Emergency, General)
 * - Tracks which announcements the current user has read
 * - Allows staff/admin to post new announcements
 * - Supports deleting announcements (admin only)
 *
 * Uses combine() and flatMapLatest() to react to filter changes automatically.
 */
class AnnouncementViewModel(
    private val repository: AnnouncementRepository
) : ViewModel() {

    private val _currentUserEmail = MutableStateFlow("")

    private val _selectedCategory = MutableStateFlow<AnnouncementCategory?>(null)
    val selectedCategory: StateFlow<AnnouncementCategory?> = _selectedCategory.asStateFlow()

    private val _showOnlyUnread = MutableStateFlow(false)
    val showOnlyUnread: StateFlow<Boolean> = _showOnlyUnread.asStateFlow()

    fun setCurrentUser(email: String) {
        _currentUserEmail.value = email
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val announcements: StateFlow<List<Announcement>> = combine(
        _selectedCategory,
        _showOnlyUnread,
        _currentUserEmail
    ) { category, onlyUnread, email ->
        Triple(category, onlyUnread, email)
    }.flatMapLatest { (category, onlyUnread, email) ->
        val flow = if (category == null) {
            repository.getAllAnnouncements(email)
        } else {
            repository.getAnnouncementsByCategory(category, email)
        }
        
        if (onlyUnread) {
            flow.map { list -> list.filter { !it.isRead } }
        } else {
            flow
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val unreadCount: StateFlow<Int> = _currentUserEmail
        .flatMapLatest { email ->
            repository.getUnreadCount(email)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    fun onCategorySelected(category: AnnouncementCategory?) {
        _selectedCategory.value = category
    }

    fun toggleUnreadFilter() {
        _showOnlyUnread.value = !_showOnlyUnread.value
    }

    fun markAsRead(announcementId: Int) {
        val email = _currentUserEmail.value
        if (email.isNotBlank()) {
            viewModelScope.launch {
                repository.markAsRead(announcementId, email)
            }
        }
    }

    fun deleteAnnouncement(announcementId: Int) {
        viewModelScope.launch {
            repository.deleteAnnouncement(announcementId)
        }
    }

    /**
     * Staff or Admin feature: Post a new announcement.
     */
    fun postAnnouncement(title: String, body: String, category: AnnouncementCategory, postedBy: String) {
        viewModelScope.launch {
            val announcement = Announcement(
                id = 0, 
                title = title,
                body = body,
                category = category,
                postedBy = postedBy,
                timestamp = System.currentTimeMillis()
            )
            repository.insert(announcement)
        }
    }

    // ── Factory ──────────────────────────────────────────────────────────────
    class Factory(private val repository: AnnouncementRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AnnouncementViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AnnouncementViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
