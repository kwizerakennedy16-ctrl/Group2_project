# Ndejje Campus Connect

> **Mobile Programming Capstone Project — BCS 2201 / BIT 2205 | Semester II 2025/2026**
> Ndejje University · Faculty of Science and Computing

---

## Project Overview

**Ndejje Campus Connect** is a mobile-first Android application that solves the fragmented information challenge at Ndejje University. Students and staff currently rely on scattered notice boards and web portals; this app unifies campus news, academic schedules, facility navigation, and university contacts into a single, accessible tool.

**Problem statement:** Students and staff at Ndejje University often face challenges accessing real-time campus information, ranging from navigating lecture room locations to receiving urgent administrative updates. This app addresses the challenge by providing one unified, offline-capable platform.

---

## Team Members & Roles

| # | Name | Role | Responsibilities |
|---|------|------|-----------------|
| 1 | **Mulindwa Wilis** | Lead Developer | Core Jetpack Compose architecture, MVVM implementation, NavHost setup, ViewModel logic |
| 2 | **Nakato Hope Keziah** | UI/UX Specialist | Material 3 styling, component design, accessibility compliance, user flow prototyping |
| 3 | **Kwizera Kennedy** | Git & Quality Manager | Repository health, branching strategy, commit message standards, code audits |
| 4 | **Namanya Tomas** | Testing & QA Engineer | Unit and integration tests, bug tracking, APK verification |
| 5 | **Namzzi Ritah** | Documentation & Research Lead | Problem analysis, proposal, README, final report, Moodle submission management |

---

## Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose + Material 3
- **Architecture:** MVVM (Model–View–ViewModel)
- **Data Persistence:** Room Database (local SQLite)
- **Navigation:** Jetpack Navigation Compose
- **Async:** Kotlin Coroutines + StateFlow
- **Testing:** JUnit 4, Mockito-Kotlin, kotlinx-coroutines-test

---

## Application Screens

| Screen | Description |
|--------|-------------|
| **Login / Onboarding** | University email authentication with real-time validation |
| **Campus News Feed** | Dynamic announcements list with category filters (Academic, Events, Emergency, General) |
| **Campus Map / Navigator** | Searchable directory of all campus facilities with category filters |
| **Academic Dashboard** | Personal timetable, exam schedule, and deadlines in a tabbed layout |
| **Resources & Directory** | University contacts (call/email actions) and downloadable PDF student guides |

---

## Architecture Diagram

```
UI Layer (Composables)
    │
    ├── LoginScreen
    ├── NewsFeedScreen
    ├── CampusMapScreen
    ├── AcademicDashboardScreen
    └── ResourcesScreen
          │
          ▼
ViewModel Layer
    ├── AuthViewModel          ← manages login state & validation
    ├── AnnouncementViewModel  ← exposes announcements via StateFlow
    └── TimetableViewModel     ← exposes lectures / exams / deadlines
          │
          ▼
Repository Layer
    ├── AnnouncementRepository
    └── TimetableRepository
          │
          ▼
Data Layer (Room Database)
    ├── AnnouncementDao
    ├── TimetableDao
    └── AppDatabase  (seeded with demo data on first launch)
```

---

## Technical Requirements Coverage

| Requirement | Implementation |
|-------------|----------------|
| Dynamic lists (LazyColumn) | All 5 screens use `LazyColumn` or `LazyRow` |
| State management | `StateFlow` + `collectAsState()` + `remember` |
| Navigation (≥ 3 screens) | 5 screens via `NavHost` + `BottomNavigation` |
| Data persistence | Room Database with 2 entities, 2 DAOs |
| Unit / integration tests | 7 test cases across `AnnouncementViewModelTest` and `AuthViewModelTest` |
| camelCase naming | Enforced across all variables and functions |
| StringResources | All user-visible text in `strings.xml` — no hardcoded strings in UI |
| Dimens extracted | All spacing, sizes, radii defined in `dimens.xml` |
| MainActivity minimal | Entry point only — sets content view, invokes `CampusConnectApp()` |

---

## Test Summary (QA Engineer: Namanya Tomas)

### Test Files
- `AnnouncementViewModelTest.kt`
- `AuthViewModelTest.kt`

### Tests Executed

| Test ID | Test Name | Function Tested | Outcome |
|---------|-----------|-----------------|---------|
| T1 | `onCategorySelected updates selectedCategory state to ACADEMIC` | Category filter | ✅ PASS |
| T2 | `onCategorySelected with null clears the category filter` | Category filter clear | ✅ PASS |
| T3 | `markAsRead calls repository markAsRead with correct id` | Repository delegation | ✅ PASS |
| T4 | `onLoginClicked with blank email sets emailError` | Email validation | ✅ PASS |
| T5 | `onLoginClicked with short password sets passwordError` | Password validation | ✅ PASS |
| T6 | `valid ndejje email results in isLoggedIn true` | Authentication flow | ✅ PASS |
| T7 | `gmail address produces loginError` | Domain restriction | ✅ PASS |

### Coverage
- Core functions tested: `onCategorySelected`, `markAsRead`, `onLoginClicked`, `validateEmail`, `validatePassword`
- Pattern: Arrange-Act-Assert with coroutine test dispatcher for deterministic results

---

## Git Workflow Guidelines (Git & Quality Manager: Kwizera Kennedy)

### Branching Strategy
```
main          ← production-ready, protected
develop       ← integration branch
feature/*     ← individual features (e.g. feature/news-feed, feature/room-db)
fix/*         ← bug fixes
```

### Commit Message Convention
```
feat(screen): add category filter chips to NewsFeedScreen
fix(db): resolve Room type converter for AnnouncementCategory
test(vm): add unit tests for AuthViewModel validation
docs(readme): update team roles and test summary
```

### Requirements
- Minimum 15 commits per member
- No direct commits to `main`
- All features merged via pull request with at least one reviewer

---

## Project Structure

```
app/src/main/java/com/ndejje/campusconnect/
├── MainActivity.kt                        ← Entry point only
├── CampusConnectApp.kt                    ← Root Composable + NavHost
├── data/
│   ├── database/
│   │   ├── AppDatabase.kt
│   │   └── dao/
│   │       ├── AnnouncementDao.kt
│   │       └── TimetableDao.kt
│   ├── model/
│   │   ├── Announcement.kt
│   │   ├── TimetableEntry.kt
│   │   └── CampusLocation.kt
│   └── repository/
│       ├── AnnouncementRepository.kt
│       └── TimetableRepository.kt
├── ui/
│   ├── navigation/
│   │   └── AppNavigation.kt
│   ├── screens/
│   │   ├── onboarding/LoginScreen.kt
│   │   ├── home/NewsFeedScreen.kt
│   │   ├── map/CampusMapScreen.kt
│   │   ├── dashboard/AcademicDashboardScreen.kt
│   │   └── resources/ResourcesScreen.kt
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── viewmodel/
    ├── AuthViewModel.kt
    ├── AnnouncementViewModel.kt
    └── TimetableViewModel.kt

app/src/test/
└── AnnouncementViewModelTest.kt
└── AuthViewModelTest.kt

app/src/main/res/
├── values/strings.xml
└── values/dimens.xml
```

---

## Setup Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/[your-org]/NdejjeCampusConnect.git
   ```
2. Open in **Android Studio Hedgehog** or later.
3. Wait for Gradle sync to complete.
4. Connect an Android device (API 26+) or start an emulator.
5. Run the app: `Shift + F10` or the Run button.
6. Run tests: right-click test file → **Run Tests**, or `./gradlew test`.

---

## Submission Checklist

- [x] GitHub repository is public
- [x] README lists all five members with roles
- [x] At least 3 distinct screens with navigation
- [x] LazyColumn used for dynamic lists
- [x] MVVM architecture implemented
- [x] Room Database for data persistence
- [x] Unit tests covering ≥ 2 core functions
- [x] All text in StringResources (no hardcoded strings)
- [x] All dimensions in dimens.xml
- [x] MainActivity.kt is entry-point only
- [ ] Minimum 15 commits per member *(in progress)*
- [ ] Signed APK uploaded to Moodle *(due 1st May 2026)*
- [ ] Individual Affective Domain Reports submitted *(due 1st May 2026)*
- [ ] Peer Evaluation completed on Moodle *(due 1st May 2026)*

---

*Built with ❤️ for Ndejje University — Solutions for a Digital Uganda.*
