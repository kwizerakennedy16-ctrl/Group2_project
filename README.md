<!-- 
  README.md for Ndejje Campus Connect
  Last updated: 6th May 2026 
-->

<h1 align="center">📱 Ndejje Campus Connect</h1>
<p align="center">
  <strong>A community‑centric mobile app for Ndejje University students and staff</strong><br>
  <em>Capstone Project – BCS 2201 / BIT 2205</em>
</p>

<div align="center">
   📱 Ndejje Campus Connect

<p align="center">
  <a href="https://youtu.be/wBouHcMpIlc" target="_blank">
    <img src="https://img.shields.io/badge/▶️_WATCH_PRESENTATION-CLICK_HERE-red?style=for-the-badge&logo=youtube&logoColor=white&labelColor=cc0000&color=aa0000" width="400">
  </a>
</p>
</div>

---

## 👥 Team Roster
| Role | Name | Student ID |
|------|------|-------------|
| **Git & QA Manager** | Kwizera Kennedy |1009027817 |
| **Documentation Lead** | Ritah Namazzi |  1008205906 |
| **UI/UX Specialist** | Nakato Hope Keziah| 1008102178|
| **Lead Developer** |Mulindwa Willis Daniel  |1010115526|
| **QA & Testing Engineer** | Namanya Tomas | 1008186620  |

---

## ✅ Feature Set

- **Secure Authentication** – Student, Staff, and Admin roles with local Room database.
- **Announcement Feed** – Filter by category (Academic, Events, Emergency, General) + unread badge.
- **Read / Unread Tracking** – Per‑user read status via SQL `LEFT JOIN`.
- **Academic Dashboard** – Lectures, exams, and deadlines in a tabbed layout.
- **Campus Map** – Searchable list of buildings with category filters (Academic, Admin, Social, Health).
- **University Resources** – One‑tap call/email contacts + downloadable PDF guides.
- **🌓 Dark / Light Mode** – Persistent user preference using DataStore, manual toggle on Home screen.
- **Professional Branding** – Ndejje University logo on Login / Register screens.

---

## 🛠 Technical Stack

| Layer | Technology |
|-------|-------------|
| Language | Kotlin |
| UI Toolkit | Jetpack Compose (Material 3) |
| Architecture | MVVM + Repository Pattern |
| Navigation | Compose Navigation |
| Database | Room (SQLite) |
| Preferences | DataStore (theme persistence) |
| Testing | JUnit4 (stubs + basic assertions) |
| Version Control | Git + GitHub (feature branches, PRs) |

---

## 🧪 QA Summary – Test Cases & Results

*Executed by Git/QA Manager & Testing Engineer – all tests passed on emulator (API 33) and physical device (Android 13).*

| Test Case ID | Description | Expected Result | Actual Result | Status |
|--------------|-------------|----------------|---------------|--------|
| TC‑AUTH‑01 | Login with valid credentials (student@ndejje.ac.ug / student123) | Navigate to Home screen | Navigated to Home screen | ✅ PASS |
| TC‑AUTH‑02 | Login with wrong password | Show error "Invalid email or password" | Error message displayed | ✅ PASS |
| TC‑AUTH‑03 | Register new student with valid data | Account created, returns to Login | Registration success, back to Login | ✅ PASS |
| TC‑ANN‑01 | Filter announcements by "Academic" | Show only academic announcements | Only academic items shown | ✅ PASS |
| TC‑ANN‑02 | Mark announcement as read | Unread badge count decreases | Count updated correctly | ✅ PASS |
| TC‑UI‑01 | Toggle dark/light mode via icon | UI switches instantly, preference saved | Mode changes and persists after restart | ✅ PASS |
| TC‑MAP‑01 | Search for "Library" | Display library location card | Location appears in filtered list | ✅ PASS |
| TC‑DB‑01 | Insert new announcement (admin) | Announcement appears in feed | New announcement visible | ✅ PASS |
| TC‑NAV‑01 | Navigate through bottom tabs | Each screen loads without crash | All tabs functional | ✅ PASS |

> **Note:** Unit tests (`SampleTest.kt`, `AuthViewModelTest.kt`) run successfully in Android Studio. Full test suite passes with 0 failures.

---

## 📌 Important Links

- **GitHub Repository:** [https://github.com/kwizerakennedy16-ctrl/Group2_project](https://github.com/kwizerakennedy16-ctrl/Group2_project)
- **YouTube Presentation:** [https://youtu.be/wBouHcMpIlc](https://youtu.be/wBouHcMpIlc) 
---


---

*Built with Love❤️❤️ for Ndejje University – Solutions for a Digital Uganda.*
