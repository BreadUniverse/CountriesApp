package com.maria.countriestotravel.di

import android.app.Application
import com.maria.countriestotravel.data.manger.LocalUserMangerImpl
import com.maria.countriestotravel.domain.manger.LocalUserManger
import com.maria.countriestotravel.domain.usecases.AppEntryUseCases
import com.maria.countriestotravel.domain.usecases.ReadAppEntry
import com.maria.countriestotravel.domain.usecases.SaveAppEntry
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {
    @Provides
    @Singleton
    fun provideLocalUserManger(
        application: Application
    ): LocalUserManger = LocalUserMangerImpl(context = application)

    @Provides
    @Singleton
    fun provideAppEntryUseCases(
        localUserManger: LocalUserManger
    ): AppEntryUseCases = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManger),
        saveAppEntry = SaveAppEntry(localUserManger)
    )
}