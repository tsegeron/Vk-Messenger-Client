package com.laru.settings.di

import com.laru.settings.data.dao.AccessTokenDao
import com.laru.settings.data.repo.AccessTokenRepo
import com.laru.settings.data.repo.AccessTokenRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface SettingsModule {

    @Binds
    @Singleton
    fun bindAccessTokenRepository(accessTokenRepoImpl: AccessTokenRepoImpl): AccessTokenRepo

}
