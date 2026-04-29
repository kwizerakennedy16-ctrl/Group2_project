package com.ndejje.campusconnect

import com.ndejje.campusconnect.viewmodel.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [AuthViewModel].
 * Authored by: Namanya Tomas (Testing and QA Engineer)
 *
 * Covers:
 *   4. Email validation rejects blank input.
 *   5. Password validation rejects passwords shorter than 6 characters.
 *   6. Valid Ndejje email results in successful login after delay.
 *   7. Non-university email results in a login error.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AuthViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * TEST 4 – Blank email shows validation error.
     *
     * Given: Email is empty and password is valid.
     * When:  onLoginClicked is triggered.
     * Then:  emailError state is not null and login is not attempted.
     */
    @Test
    fun `onLoginClicked with blank email sets emailError`() = runTest {
        // Given
        viewModel.onEmailChanged("")
        viewModel.onPasswordChanged("secure123")

        // When
        viewModel.onLoginClicked()
        advanceUntilIdle()

        // Then
        assertNotNull(viewModel.uiState.value.emailError)
        assertTrue(viewModel.uiState.value.emailError!!.isNotBlank())
    }

    /**
     * TEST 5 – Short password shows validation error.
     *
     * Given: Email is valid but password has fewer than 6 characters.
     * When:  onLoginClicked is triggered.
     * Then:  passwordError state is not null.
     */
    @Test
    fun `onLoginClicked with short password sets passwordError`() = runTest {
        // Given
        viewModel.onEmailChanged("student@ndejjeuniversity.ac.ug")
        viewModel.onPasswordChanged("abc")

        // When
        viewModel.onLoginClicked()
        advanceUntilIdle()

        // Then
        assertNotNull(viewModel.uiState.value.passwordError)
    }

    /**
     * TEST 6 – Valid Ndejje email produces successful login.
     *
     * Given: Email ends with @ndejjeuniversity.ac.ug and password is >= 6 chars.
     * When:  onLoginClicked is triggered and coroutine delay elapses.
     * Then:  isLoggedIn becomes true and no errors are present.
     */
    @Test
    fun `valid ndejje email results in isLoggedIn true`() = runTest {
        // Given
        viewModel.onEmailChanged("bcs2201@ndejjeuniversity.ac.ug")
        viewModel.onPasswordChanged("password123")

        // When
        viewModel.onLoginClicked()
        advanceUntilIdle()   // advances past the 1500 ms simulated delay

        // Then
        assertTrue(viewModel.uiState.value.isLoggedIn)
        assertNull(viewModel.uiState.value.loginError)
    }

    /**
     * TEST 7 – Non-university email produces loginError.
     *
     * Given: Email is valid format but not a Ndejje domain.
     * When:  onLoginClicked is triggered.
     * Then:  loginError is set and isLoggedIn remains false.
     */
    @Test
    fun `gmail address produces loginError`() = runTest {
        // Given
        viewModel.onEmailChanged("student@gmail.com")
        viewModel.onPasswordChanged("password123")

        // When
        viewModel.onLoginClicked()
        advanceUntilIdle()

        // Then
        assertNotNull(viewModel.uiState.value.loginError)
        assertTrue(!viewModel.uiState.value.isLoggedIn)
    }
}
