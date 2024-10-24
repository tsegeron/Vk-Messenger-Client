package com.laru.data.di

import com.laru.data.NetworkConnectionManager
import com.laru.data.NetworkConnectionManagerImpl
import com.laru.data.repository.AuthRepository
import com.laru.data.repository.AuthRepositoryImpl
import com.laru.data.repository.ChatRepository
import com.laru.data.repository.ChatRepositoryImpl
import com.laru.data.repository.ChatsRepository
import com.laru.data.repository.ChatsRepositoryImpl
import com.laru.data.repository.FriendsRepository
import com.laru.data.repository.FriendsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    @Binds
    fun bindNetworkConnectivityManager(networkConnectionManagerImpl: NetworkConnectionManagerImpl): NetworkConnectionManager

    @Binds
    @Singleton
    fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun bindChatRepository(chatRepositoryImpl: ChatRepositoryImpl): ChatRepository

    @Binds
    @Singleton
    fun bindChatsRepository(chatsRepositoryImpl: ChatsRepositoryImpl): ChatsRepository

    @Binds
    @Singleton
    fun bindFriendsRepository(friendsRepositoryImpl: FriendsRepositoryImpl): FriendsRepository

}
