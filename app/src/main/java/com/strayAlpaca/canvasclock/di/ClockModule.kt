package com.strayAlpaca.canvasclock.di

import com.strayAlpaca.data.repository_impl.ClockRepositoryImpl
import com.strayAlpaca.data.repository_impl.FaqRepositoryImpl
import com.strayAlpaca.domain.repository.ClockRepository
import com.strayAlpaca.domain.repository.FaqRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ClockModule {
    @Binds
    abstract fun bindClockRepository( clockRepositoryImpl: ClockRepositoryImpl ) : ClockRepository

    @Binds
    abstract fun bindFaqRepository ( faqRepository: FaqRepositoryImpl) : FaqRepository
}