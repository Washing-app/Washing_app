package com.example.data.di

import com.example.data.repository.BookingRepositoryImpl
import com.example.domain.repository.BookingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BookingModule {

    @Binds
    abstract fun bindBookingRepository(
        impl: BookingRepositoryImpl
    ): BookingRepository
}