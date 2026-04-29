package com.ndejje.campusconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ndejje.campusconnect.data.model.TimetableEntry
import com.ndejje.campusconnect.data.repository.TimetableRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class TimetableViewModel(
    private val repository: TimetableRepository
) : ViewModel() {

    val lectures: StateFlow<List<TimetableEntry>> = repository.getLectures()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val exams: StateFlow<List<TimetableEntry>> = repository.getExams()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val deadlines: StateFlow<List<TimetableEntry>> = repository.getDeadlines()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // ── Factory ──────────────────────────────────────────────────────────────
    class Factory(private val repository: TimetableRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TimetableViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TimetableViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
