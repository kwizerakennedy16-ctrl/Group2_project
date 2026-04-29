package com.ndejje.campusconnect.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ndejje.campusconnect.data.database.dao.AnnouncementDao
import com.ndejje.campusconnect.data.database.dao.TimetableDao
import com.ndejje.campusconnect.data.database.dao.UserDao
import com.ndejje.campusconnect.data.model.Announcement
import com.ndejje.campusconnect.data.model.AnnouncementCategory
import com.ndejje.campusconnect.data.model.AnnouncementReadStatus
import com.ndejje.campusconnect.data.model.TimetableEntry
import com.ndejje.campusconnect.data.model.TimetableType
import com.ndejje.campusconnect.data.model.User
import com.ndejje.campusconnect.viewmodel.UserRole
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// ── Type converters ──────────────────────────────────────────────────────────
class Converters {
    @TypeConverter
    fun fromAnnouncementCategory(value: AnnouncementCategory): String = value.name

    @TypeConverter
    fun toAnnouncementCategory(value: String): AnnouncementCategory =
        AnnouncementCategory.valueOf(value)

    @TypeConverter
    fun fromTimetableType(value: TimetableType): String = value.name

    @TypeConverter
    fun toTimetableType(value: String): TimetableType = TimetableType.valueOf(value)
    
    @TypeConverter
    fun fromUserRole(value: UserRole): String = value.name

    @TypeConverter
    fun toUserRole(value: String): UserRole = UserRole.valueOf(value)
}

// ── Database ─────────────────────────────────────────────────────────────────
@Database(
    entities = [
        Announcement::class, 
        TimetableEntry::class, 
        AnnouncementReadStatus::class,
        User::class
    ],
    version = 9, 
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun announcementDao(): AnnouncementDao
    abstract fun timetableDao(): TimetableDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }

        private fun buildDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "campus_connect.db"
            )
                .fallbackToDestructiveMigration()
                .addCallback(object : Callback() {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        instance?.let { database ->
                            CoroutineScope(Dispatchers.IO).launch {
                                checkAndSeedData(database)
                            }
                        }
                    }
                })
                .build()

        private suspend fun checkAndSeedData(database: AppDatabase) {
            val announcementDao = database.announcementDao()
            val userDao = database.userDao()

            // Seed announcements if empty
            val existingAnnouncements = announcementDao.getAllAnnouncements("").first()
            if (existingAnnouncements.isEmpty()) {
                seedDemoData(database)
            }

            // Seed default Admin & Staff accounts if they don't exist
            if (userDao.getUserByEmail("admin@ndejje.ac.ug") == null) {
                userDao.registerUser(User("admin@ndejje.ac.ug", "admin123", "System Admin", "ADMIN001", UserRole.ADMIN))
            }
            if (userDao.getUserByEmail("staff@ndejje.ac.ug") == null) {
                userDao.registerUser(User("staff@ndejje.ac.ug", "staff123", "Lecturer Alex", "STAFF001", UserRole.STAFF))
            }
        }

        private suspend fun seedDemoData(database: AppDatabase) {
            val announcementDao = database.announcementDao()
            val timetableDao = database.timetableDao()

            announcementDao.insertAll(
                listOf(
                    Announcement(
                        title = "Semester II Exams Timetable Released",
                        body = "The official examination timetable for Semester II 2025/2026 is now available on the university portal. All students are required to confirm their exam venues by 25th April.",
                        postedBy = "Academic Registrar",
                        category = AnnouncementCategory.ACADEMIC
                    ),
                    Announcement(
                        title = "Capstone MVP Review – 21st April",
                        body = "All BCS 2201 and BIT 2205 groups must attend the Minimum Viable Product review scheduled for 21st April 2026. Attendance is mandatory. Venue: Computer Lab 3, Block B.",
                        postedBy = "Luyima Alex Cedric",
                        category = AnnouncementCategory.ACADEMIC
                    ),
                    Announcement(
                        title = "Inter-University Sports Gala",
                        body = "Ndejje University will host the Inter-University Sports Gala on 28th April 2026. All students are encouraged to participate. Registration closes 24th April.",
                        postedBy = "Student Guild President",
                        category = AnnouncementCategory.EVENTS
                    ),
                    Announcement(
                        title = "Water Supply Disruption – Main Campus",
                        body = "Due to ongoing maintenance, water supply to the Main Campus hostels will be disrupted on 22nd April from 08:00 to 14:00. We apologise for the inconvenience.",
                        postedBy = "Estates & Works Department",
                        category = AnnouncementCategory.EMERGENCY
                    ),
                    Announcement(
                        title = "Library Extended Hours",
                        body = "The university library will operate extended hours (07:00–22:00) during the revision period, 19th April to 1st May 2026.",
                        postedBy = "Chief Librarian",
                        category = AnnouncementCategory.GENERAL
                    )
                )
            )

            timetableDao.insertAll(
                listOf(
                    TimetableEntry(
                        courseCode = "BCS 2201",
                        courseName = "Mobile Programming",
                        lecturer = "Luyima Alex Cedric",
                        venue = "Computer Lab 3, Block B",
                        dayOfWeek = "Monday",
                        startTime = "08:00",
                        endTime = "10:00",
                        entryType = TimetableType.LECTURE
                    ),
                    TimetableEntry(
                        courseCode = "BCS 2203",
                        courseName = "Software Engineering",
                        lecturer = "Dr. Kato Brian",
                        venue = "Lecture Hall 2",
                        dayOfWeek = "Tuesday",
                        startTime = "10:00",
                        endTime = "12:00",
                        entryType = TimetableType.LECTURE
                    ),
                    TimetableEntry(
                        courseCode = "BCS 2205",
                        courseName = "Database Systems II",
                        lecturer = "Ms. Nakamya Sarah",
                        venue = "Lecture Hall 4",
                        dayOfWeek = "Wednesday",
                        startTime = "14:00",
                        endTime = "16:00",
                        entryType = TimetableType.LECTURE
                    ),
                    TimetableEntry(
                        courseCode = "BCS 2201",
                        courseName = "Mobile Programming",
                        lecturer = "Luyima Alex Cedric",
                        venue = "Examination Hall A",
                        dayOfWeek = "Thursday",
                        startTime = "09:00",
                        endTime = "12:00",
                        entryType = TimetableType.EXAM
                    ),
                    TimetableEntry(
                        courseCode = "BCS 2201",
                        courseName = "Mobile Programming",
                        lecturer = "Luyima Alex Cedric",
                        venue = "Moodle (Online)",
                        dayOfWeek = "Thursday",
                        startTime = "23:59",
                        endTime = "23:59",
                        entryType = TimetableType.DEADLINE
                    )
                )
            )
        }
    }
}
