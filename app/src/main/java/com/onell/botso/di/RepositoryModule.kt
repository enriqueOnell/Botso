package com.onell.botso.di

import com.onell.botso.data.repository.UniRepositoryImpl
import com.onell.botso.domain.repository.UniRepository
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
    abstract fun bindUniRepository(
        uniRepositoryImpl: UniRepositoryImpl
    ): UniRepository
}
