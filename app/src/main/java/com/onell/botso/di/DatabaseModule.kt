package com.onell.botso.di

import android.content.Context
import com.onell.botso.data.local.BotsoDataBase
import com.onell.botso.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Hilt que provee la base de datos Room y sus DAOs.
 *
 * `BotsoDataBase` se crea una sola vez (`@Singleton`) y los DAOs se obtienen de
 * ella, de modo que repositorios y casos de uso reciben las dependencias ya
 * construidas.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideBotsoDatabase(@ApplicationContext context: Context): BotsoDataBase {
        return BotsoDataBase.getDatabase(context)
    }

    @Provides
    fun provideSemesterDao(database: BotsoDataBase): SemesterDao = database.semesterDao()

    @Provides
    fun provideCourseDao(database: BotsoDataBase): CourseDao = database.courseDao()

    @Provides
    fun provideGradeDao(database: BotsoDataBase): GradeDao = database.gradeDao()

    @Provides
    fun provideTaskDao(database: BotsoDataBase): TaskDao = database.taskDao()

    @Provides
    fun provideClassSessionDao(database: BotsoDataBase): ClassSessionDao = database.classSessionDao()
}