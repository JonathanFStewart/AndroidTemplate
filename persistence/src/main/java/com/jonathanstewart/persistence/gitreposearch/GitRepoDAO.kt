package com.jonathanstewart.persistence.gitreposearch

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
internal interface GitRepoDAO {

    /**
     * Get all the repos stored in the local DB
     */
    @Query("SELECT * FROM GitRepos ORDER BY stars DESC ")
    fun getAll(): List<GitRepoEntity>

    /**
     * Store a repo in the local DB
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gitRepoEntity: GitRepoEntity)

    /**
     * Insert a List of Repos into the local DB
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAll(kist: List<GitRepoEntity>)

    /**
     * Clear the Local DB of all Repos
     */
    @Query("DELETE FROM GitRepos")
    fun deleteAll()
}

