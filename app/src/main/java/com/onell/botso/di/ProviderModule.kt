package com.onell.botso.di

import com.onell.botso.data.provider.SemesterPdfProviderImpl
import com.onell.botso.domain.provider.SemesterPdfProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProviderModule {
    @Binds
    @Singleton
    abstract fun bindSemesterPdfProvider(impl: SemesterPdfProviderImpl): SemesterPdfProvider
}
