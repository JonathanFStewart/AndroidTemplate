package com.jonathanstewart.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jonathanstewart.persistence.gitreposearch.*

/**
 * Room Database for the app
 */
@Database(entities = [SearchQuery::class, GitRepoEntity::class, GitOwnerEntity::class], version = 1)
internal abstract class AppDatabase: RoomDatabase(){
    abstract fun searchQueryDAO(): SearchQueryDAO
    abstract fun gitRepoDAO() : GitRepoDAO }