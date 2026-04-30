package com.ndejje.campusconnect.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ndejje.campusconnect.data.database.dao.UserDao
import com.ndejje.campusconnect.data.model.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
// TODO: Implement password hashing for production
/**
 * Defines the types of users in the system.
 */
enum class UserRole {
    STUDENT, STAFF, ADMIN
}

/**
 * Represent the UI state for Authentication screens (Login & Register).
 */
data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val userRole: UserRole = UserRole.STUDENT,
    val emailError: String? = null,
    val passwordError: String? = null,
    val loginError: String? = null,
    val registerError: String? = null,
    val registrationSuccess: Boolean = false
)

/**
 * ViewModel responsible for handling authentication logic and state management.
 */
class AuthViewModel(private val userDao: UserDao) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onEmailChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            email = value, 
            emailError = null, 
            loginError = null, 
            registerError = null
        )
    }

    fun onPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            password = value, 
            passwordError = null, 
            loginError = null, 
            registerError = null
        )
    }

    /**
     * Handles the login action by verifying credentials against the local database.
     */
    fun onLoginClicked() {
        val currentState = _uiState.value
        val emailError = validateEmail(currentState.email)
        val passwordError = validatePassword(currentState.password)

        if (emailError != null || passwordError != null) {
            _uiState.value = currentState.copy(
                emailError = emailError,
                passwordError = passwordError
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = currentState.copy(isLoading = true)
            delay(800L) // Simulate network delay
            
            val email = currentState.email.lowercase().trim()
            val password = currentState.password

            val user = userDao.getUserByEmail(email)

            if (user != null && user.password == password) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false, 
                    isLoggedIn = true, 
                    userRole = user.role,
                    email = user.email // Ensure email in state matches database
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    loginError = "Invalid email or password. Please try again."
                )
            }
        }
    }

    /**
     * Handles user registration by saving details to the local database.
     */
    fun onRegisterClicked(name: String, studentId: String, email: String, password: String) {
        if (name.isBlank() || studentId.isBlank()) {
            _uiState.value = _uiState.value.copy(registerError = "All fields are required")
            return
        }
        
        val emailError = validateEmail(email)
        val passwordError = validatePassword(password)

        if (emailError != null || passwordError != null) {
            _uiState.value = _uiState.value.copy(
                registerError = emailError ?: passwordError
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            delay(1000L)
            
            val normalizedEmail = email.lowercase().trim()
            
            try {
                // Check if user already exists
                val existingUser = userDao.getUserByEmail(normalizedEmail)
                if (existingUser != null) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        registerError = "An account with this email already exists."
                    )
                    return@launch
                }

                // Create new user
                val newUser = User(
                    email = normalizedEmail,
                    password = password,
                    name = name,
                    studentId = studentId,
                    role = UserRole.STUDENT
                )
                
                userDao.registerUser(newUser)
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    registrationSuccess = true,
                    email = normalizedEmail
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    registerError = "Registration failed: ${e.localizedMessage}"
                )
            }
        }
    }
    
    fun resetRegistrationStatus() {
        _uiState.value = _uiState.value.copy(registrationSuccess = false, registerError = null)
    }

    fun onLogout() {
        _uiState.value = AuthUiState()
    }

    private fun validateEmail(email: String): String? {
        if (email.isBlank()) return "Email cannot be empty"
        // Simplified pattern for compatibility without android.util.Patterns if needed, 
        // but since this is an Android project, we'll stick to it.
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-z]+$".toRegex()
        if (!email.matches(emailRegex)) {
            return "Enter a valid email address"
        }
        return null
    }

    private fun validatePassword(password: String): String? {
        if (password.isBlank()) return "Password cannot be empty"
        if (password.length < 6) return "Password must be at least 6 characters"
        return null
    }

    // ── Factory ──────────────────────────────────────────────────────────────
    class Factory(private val userDao: UserDao) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AuthViewModel(userDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
