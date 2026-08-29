package com.onell.botso.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.onell.botso.data.local.dao.*
import com.onell.botso.data.local.entity.*

@Database(
    entities = [
        Semester::class,
        Course::class,
        Grade::class,
        Task::class,
        ClassSession::class
    ],
    version = 7,
    exportSchema = false
)
abstract class UniDatabase : RoomDatabase() {
    abstract fun semesterDao(): SemesterDao
    abstract fun courseDao(): CourseDao
    abstract fun gradeDao(): GradeDao
    abstract fun taskDao(): TaskDao
    abstract fun classSessionDao(): ClassSessionDao

    companion object {
        const val DATABASE_NAME = "uni_database"
    }
}
