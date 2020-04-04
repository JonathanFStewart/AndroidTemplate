package com.jonathanstewart.persistence.gitreposearch

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
/**
 * Data class / Room Entity representing a Search Query in the persistince module
 * this class is internal as all interactions with the persistence layer should be done through the exposed persistence services
 */
@Entity(tableName = "Queries", indices = [Index(value = ["query_string"], unique = true)])
 internal data class SearchQuery(
    @PrimaryKey (autoGenerate = true) var queryId: Long?,
    @ColumnInfo(name = "query_string")
    var query: String
)

