package com.onell.UniApp.di

import android.content.Context
import androidx.room.Room
import com.onell.UniApp.data.local.UniDatabase
import com.onell.UniApp.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): UniDatabase {
        return Room.databaseBuilder(
            context,
            UniDatabase::class.java,
            UniDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideSemesterDao(db: UniDatabase): SemesterDao = db.semesterDao()

    @Provides
    fun provideCourseDao(db: UniDatabase): CourseDao = db.courseDao()

    @Provides
    fun provideGradeDao(db: UniDatabase): GradeDao = db.gradeDao()

    @Provides
    fun provideTaskDao(db: UniDatabase): TaskDao = db.taskDao()

    @Provides
    fun provideClassSessionDao(db: UniDatabase): ClassSessionDao = db.classSessionDao()
}
