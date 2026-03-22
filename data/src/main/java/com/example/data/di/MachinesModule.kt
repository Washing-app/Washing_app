package com.example.data.di

import com.example.domain.repository.MachinesRepository
import com.example.data.repository.MachinesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MachinesModule {

    @Binds
    abstract fun bindMachinesRepository(
        impl: MachinesRepositoryImpl
    ): MachinesRepository
}