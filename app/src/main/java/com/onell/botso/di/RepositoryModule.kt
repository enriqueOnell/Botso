package com.onell.botso.di

import com.onell.botso.data.repository.*
import com.onell.botso.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSemesterRepository(impl: SemesterRepositoryImpl): SemesterRepository

    @Binds
    @Singleton
    abstract fun bindCourseRepository(impl: CourseRepositoryImpl): CourseRepository

    @Binds
    @Singleton
    abstract fun bindGradeRepository(impl: GradeRepositoryImpl): GradeRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(impl: TaskRepositoryImpl): TaskRepository

    @Binds
    @Singleton
    abstract fun bindClassSessionRepository(impl: ClassSessionRepositoryImpl): ClassSessionRepository
}