package com.jonathanstewart.persistence.gitreposearch

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
internal interface SearchQueryDAO {
    /**
     * Get all the search queries and sort them by their auto generated primary key so that the most recent query is first
     */
    @Query("SELECT * FROM Queries ORDER BY queryId DESC")
    fun getAll():LiveData<List<SearchQuery>>
    /**
     * Insert a query
     */
    @Insert(onConflict = REPLACE)
    fun insert(searchQuery: SearchQuery)
    /**
     * Get the most recent query
     */
    @Query("SELECT * FROM Queries WHERE queryId = (SELECT max(queryId) FROM Queries)")
    fun getLatestQuery():LiveData<SearchQuery>
}