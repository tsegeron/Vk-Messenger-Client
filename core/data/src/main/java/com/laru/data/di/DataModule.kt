package com.laru.data.di

import com.laru.data.repository.AuthRepository
import com.laru.data.repository.AuthRepositoryImpl
import com.laru.data.repository.ChatRepository
import com.laru.data.repository.ChatRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    @Binds
    @Singleton
    fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun provideChatRepository(chatRepositoryImpl: ChatRepositoryImpl): ChatRepository
}
