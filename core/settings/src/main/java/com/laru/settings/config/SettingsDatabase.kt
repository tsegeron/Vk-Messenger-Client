package com.laru.settings.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.laru.settings.data.dao.AccessTokenDao
import com.laru.settings.data.entity.AccessTokenEntity


@Database(
    entities = [
        AccessTokenEntity::class,
    ],
    version = 1,
)
abstract class SettingsDatabase: RoomDatabase() {
    abstract fun accessTokenDao(): AccessTokenDao
}
