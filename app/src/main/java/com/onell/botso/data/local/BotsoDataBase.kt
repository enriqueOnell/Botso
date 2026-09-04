package com.onell.botso.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.onell.botso.data.local.dao.*
import com.onell.botso.data.local.entity.*

@Database(
    entities = [
        SemesterEntity::class,
        CourseEntity::class,
        GradeEntity::class,
        TaskEntity::class,
        ClassSessionEntity::class
    ],
    version = 7,
    exportSchema = false
)
abstract class BotsoDataBase : RoomDatabase() {
    abstract fun semesterDao(): SemesterDao
    abstract fun courseDao(): CourseDao
    abstract fun gradeDao(): GradeDao
    abstract fun taskDao(): TaskDao
    abstract fun classSessionDao(): ClassSessionDao

    companion object {
        @Volatile
        private var INSTANCE: BotsoDataBase? = null
        fun getDatabase(context: Context): BotsoDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BotsoDataBase::class.java,
                    "botso_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
