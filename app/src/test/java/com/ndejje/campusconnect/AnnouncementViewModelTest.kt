package com.ndejje.campusconnect

import com.ndejje.campusconnect.data.model.Announcement
import com.ndejje.campusconnect.data.model.AnnouncementCategory
import com.ndejje.campusconnect.data.repository.AnnouncementRepository
import com.ndejje.campusconnect.viewmodel.AnnouncementViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Unit tests for [AnnouncementViewModel].
 * Authored by: Namanya Tomas (Testing and QA Engineer)
 *
 * Covers:
 *   1. Category filter selection updates state correctly.
 *   2. markAsRead delegates to repository correctly.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AnnouncementViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var repository: AnnouncementRepository
    private lateinit var viewModel: AnnouncementViewModel

    private val sampleAnnouncements = listOf(
        Announcement(
            id = 1,
            title = "Exam Timetable Released",
            body = "Check the portal for your exam schedule.",
            postedBy = "Academic Registrar",
            category = AnnouncementCategory.ACADEMIC
        ),
        Announcement(
            id = 2,
            title = "Sports Gala",
            body = "Register for the inter-university sports gala.",
            postedBy = "Student Guild",
            category = AnnouncementCategory.EVENTS
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
        // Default stubs
        whenever(repository.getAllAnnouncements()).thenReturn(flowOf(sampleAnnouncements))
        whenever(repository.getAnnouncementsByCategory(AnnouncementCategory.ACADEMIC))
            .thenReturn(flowOf(listOf(sampleAnnouncements[0])))
        whenever(repository.getUnreadCount()).thenReturn(flowOf(2))

        viewModel = AnnouncementViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * TEST 1 – Category filter selection.
     *
     * Given: The ViewModel is initialised with no category selected.
     * When:  onCategorySelected is called with ACADEMIC.
     * Then:  selectedCategory state should equal ACADEMIC.
     */
    @Test
    fun `onCategorySelected updates selectedCategory state to ACADEMIC`() = runTest {
        // Given – initial state has no category selected
        assertNull(viewModel.selectedCategory.value)

        // When
        viewModel.onCategorySelected(AnnouncementCategory.ACADEMIC)
        advanceUntilIdle()

        // Then
        assertEquals(AnnouncementCategory.ACADEMIC, viewModel.selectedCategory.value)
    }

    /**
     * TEST 2 – Clearing the category filter.
     *
     * Given: A category is currently selected.
     * When:  onCategorySelected is called with null.
     * Then:  selectedCategory state should be null (show all announcements).
     */
    @Test
    fun `onCategorySelected with null clears the category filter`() = runTest {
        // Given
        viewModel.onCategorySelected(AnnouncementCategory.EVENTS)
        advanceUntilIdle()
        assertEquals(AnnouncementCategory.EVENTS, viewModel.selectedCategory.value)

        // When
        viewModel.onCategorySelected(null)
        advanceUntilIdle()

        // Then
        assertNull(viewModel.selectedCategory.value)
    }

    /**
     * TEST 3 – markAsRead delegates to repository.
     *
     * Given: An announcement with id = 1.
     * When:  markAsRead(1) is called on the ViewModel.
     * Then:  repository.markAsRead(1) must be invoked exactly once.
     */
    @Test
    fun `markAsRead calls repository markAsRead with correct id`() = runTest {
        // When
        viewModel.markAsRead(1)
        advanceUntilIdle()

        // Then
        verify(repository).markAsRead(1)
    }
}
