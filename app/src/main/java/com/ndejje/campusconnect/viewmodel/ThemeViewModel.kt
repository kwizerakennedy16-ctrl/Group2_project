package com.ndejje.campusconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ndejje.campusconnect.data.ThemePreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(private val themePreferences: ThemePreferences) : ViewModel() {

    val isDarkTheme: StateFlow<Boolean?> = themePreferences.isDarkTheme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            themePreferences.saveTheme(isDark)
        }
    }

    class Factory(private val themePreferences: ThemePreferences) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ThemeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ThemeViewModel(themePreferences) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
