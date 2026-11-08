package com.onell.botso.di

import com.onell.botso.data.provider.SemesterPdfProviderImpl
import com.onell.botso.domain.provider.SemesterPdfProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo Hilt que enlaza proveedores de infraestructura.
 *
 * Actualmente conecta `SemesterPdfProviderImpl` con la interfaz
 * `SemesterPdfProvider` usada por el caso de uso de reportes.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ProviderModule {
    @Binds
    @Singleton
    abstract fun bindSemesterPdfProvider(impl: SemesterPdfProviderImpl): SemesterPdfProvider
}
