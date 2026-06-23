package com.ndejje.campusconnect

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ndejje.campusconnect.R
import com.ndejje.campusconnect.data.ThemePreferences
import com.ndejje.campusconnect.data.database.AppDatabase
import com.ndejje.campusconnect.data.repository.AnnouncementRepository
import com.ndejje.campusconnect.data.repository.TimetableRepository
import com.ndejje.campusconnect.ui.navigation.Screen
import com.ndejje.campusconnect.ui.navigation.bottomNavItems
import com.ndejje.campusconnect.ui.screens.dashboard.AcademicDashboardScreen
import com.ndejje.campusconnect.ui.screens.home.NewsFeedScreen
import com.ndejje.campusconnect.ui.screens.map.CampusMapScreen
import com.ndejje.campusconnect.ui.screens.onboarding.LoginScreen
import com.ndejje.campusconnect.ui.screens.onboarding.RegisterScreen
import com.ndejje.campusconnect.ui.screens.resources.ResourcesScreen
import com.ndejje.campusconnect.ui.theme.CampusConnectTheme
import com.ndejje.campusconnect.viewmodel.AnnouncementViewModel
import com.ndejje.campusconnect.viewmodel.AuthViewModel
import com.ndejje.campusconnect.viewmodel.ThemeViewModel
import com.ndejje.campusconnect.viewmodel.TimetableViewModel

/**
 * Root Composable for the Ndejje Campus Connect application.
 *
 * This is the entry point of the UI layer. It sets up:
 *
 * **Navigation:**
 * - NavController for screen navigation
 * - Bottom navigation bar with 4 tabs (Home, Map, Dashboard, Resources)
 * - Conditional bottom bar (hidden on login/register screens)
 *
 * **Dependency Injection:**
 * - Database instance (Room)
 * - Repositories (Announcement, Timetable)
 * - ViewModels with custom factories
 *
 * **State Management:**
 * - Authentication state from AuthViewModel
 * - Announcements with filtering from AnnouncementViewModel
 * - Timetable data from TimetableViewModel
 *
 * **Screen Routes:**
 * - Login -> Register -> Home (after successful auth)
 * - Home, Map, Dashboard, Resources (main app screens)
 */
@Composable
fun CampusConnectApp() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val database = AppDatabase.getInstance(context)
    val announcementRepository = AnnouncementRepository(database.announcementDao())
    val timetableRepository = TimetableRepository(database.timetableDao())
    val userDao = database.userDao()
    
    val themePreferences = remember { ThemePreferences(context) }
    val themeViewModel: ThemeViewModel = viewModel(
        factory = ThemeViewModel.Factory(themePreferences)
    )

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModel.Factory(userDao)
    )
    val announcementViewModel: AnnouncementViewModel = viewModel(
        factory = AnnouncementViewModel.Factory(announcementRepository)
    )
    val timetableViewModel: TimetableViewModel = viewModel(
        factory = TimetableViewModel.Factory(timetableRepository)
    )

    val isDarkThemePref by themeViewModel.isDarkTheme.collectAsState()
    val isDarkTheme = isDarkThemePref ?: isSystemInDarkTheme()

    CampusConnectTheme(darkTheme = isDarkTheme) {
        val authState by authViewModel.uiState.collectAsState()
        
        // Update the current user in AnnouncementViewModel whenever authState changes
        LaunchedEffect(authState.email) {
            announcementViewModel.setCurrentUser(authState.email)
        }

        val announcements by announcementViewModel.announcements.collectAsState()
        val selectedCategory by announcementViewModel.selectedCategory.collectAsState()
        val unreadCount by announcementViewModel.unreadCount.collectAsState()
        val isFilteringUnread by announcementViewModel.showOnlyUnread.collectAsState()
        val lectures by timetableViewModel.lectures.collectAsState()
        val exams by timetableViewModel.exams.collectAsState()
        val deadlines by timetableViewModel.deadlines.collectAsState()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val showBottomBar = currentDestination?.route != Screen.Login.route && currentDestination?.route != Screen.Register.route

        Scaffold(
            bottomBar = {
                if (showBottomBar) {
                    NavigationBar {
                        bottomNavItems.forEach { item ->
                            NavigationBarItem(
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = stringResource(
                                            R.string.cd_bottom_nav_icon,
                                            item.label
                                        )
                                    )
                                },
                                label = { Text(item.label) },
                                selected = currentDestination?.hierarchy?.any {
                                    it.route == item.screen.route
                                } == true,
                                onClick = {
                                    navController.navigate(item.screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Login.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Login.route) {
                    LoginScreen(
                        uiState = authState,
                        onEmailChanged = authViewModel::onEmailChanged,
                        onPasswordChanged = authViewModel::onPasswordChanged,
                        onLoginClicked = authViewModel::onLoginClicked,
                        onNavigateToMain = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        },
                        onRegisterClicked = {
                            navController.navigate(Screen.Register.route)
                        }
                    )
                }

                composable(Screen.Register.route) {
                    RegisterScreen(
                        uiState = authState,
                        onRegisterClicked = authViewModel::onRegisterClicked,
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        onRegistrationSuccess = {
                            navController.popBackStack()
                            authViewModel.resetRegistrationStatus()
                        }
                    )
                }

                composable(Screen.Home.route) {
                    NewsFeedScreen(
                        announcements = announcements,
                        selectedCategory = selectedCategory,
                        unreadCount = unreadCount,
                        userRole = authState.userRole,
                        isDarkTheme = isDarkTheme,
                        onThemeToggle = { themeViewModel.toggleTheme(!isDarkTheme) },
                        onCategorySelected = announcementViewModel::onCategorySelected,
                        onAnnouncementClicked = { announcement ->
                            announcementViewModel.markAsRead(announcement.id)
                        },
                        onPostAnnouncement = { title, body, category ->
                            announcementViewModel.postAnnouncement(
                                title = title,
                                body = body,
                                category = category,
                                postedBy = if (authState.userRole == com.ndejje.campusconnect.viewmodel.UserRole.ADMIN) "Administrator" else "Staff Member"
                            )
                        },
                        onLogout = {
                            authViewModel.onLogout()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        isFilteringUnread = isFilteringUnread,
                        onToggleUnreadFilter = announcementViewModel::toggleUnreadFilter,
                        onDeleteAnnouncement = announcementViewModel::deleteAnnouncement
                    )
                }

                composable(Screen.Map.route) {
                    CampusMapScreen()
                }

                composable(Screen.Dashboard.route) {
                    AcademicDashboardScreen(
                        lectures = lectures,
                        exams = exams,
                        deadlines = deadlines,
                        onLogout = {
                            authViewModel.onLogout()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }

                composable(Screen.Resources.route) {
                    ResourcesScreen()
                }
            }
        }
    }
}
