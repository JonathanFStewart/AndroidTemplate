package com.jonathanstewart.persistence

import android.content.Context
import androidx.room.Room

/**
 * Instantiates the room database for the app
 */
internal object AppDatabaseFactory {
    internal fun provideDatabase (applicationContext: Context): AppDatabase{
        return Room.databaseBuilder(applicationContext,AppDatabase::class.java,"app-database").build()
    }
}