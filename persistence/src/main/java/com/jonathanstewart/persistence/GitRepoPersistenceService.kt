package com.jonathanstewart.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.jonathanstewart.data.GitRepo
import com.jonathanstewart.persistence.gitreposearch.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface GitRepoPersistenceService {

    suspend fun addQuery(query: String)
    fun getAllQueries(): LiveData<List<String>>
    fun getLatestQuery(): LiveData<String?>
    suspend fun addRepoSearchResults(results: List<GitRepo>, query: String)
    suspend fun getAllRepos(): List<GitRepo>
}

/**
 * Class that exposes the available actions that can be taken on the Local DB
 * ALl transactions with the  persistence layer should be made through services like this
 * @param searchQueryDAO is the Room DAO for the Search queries
 * @param gitRepoDAO is the Room DAO for Git Repos
 */
class GitRepoPersistenceServiceImpl internal constructor(
    private val searchQueryDAO: SearchQueryDAO,
    private val gitRepoDAO: GitRepoDAO): GitRepoPersistenceService {

    /**
     * Adds a query to the query DB Table
     */
    override suspend fun addQuery(query :String){
        if (query.isNotBlank()) {
            withContext(Dispatchers.IO) { searchQueryDAO.insert(SearchQuery(query = query, queryId = null)) }
        }
    }
    /**
     * Retrieves all of the queries from the DB in order and presents them as a LiveData of a list of Strings
     */
   override fun getAllQueries () : LiveData<List<String>>{
         return Transformations.map(searchQueryDAO.getAll()){it.map { it.query }}
    }

    /**
     * Provides the most recent query as a live data
     */
    override fun getLatestQuery(): LiveData<String?> {
        return Transformations.map(searchQueryDAO.getLatestQuery()){it?.query}
    }

    /**
     * Clears all of the Git Repositories from the DB and then Inserts the new given list
     * @param results is the new list of repositories to be stored
     * @param query is the Query that was used to acquire the new repos, this is included in the DB so that
     * we can know when to refresh the data in the DB on subsequent queries
     */
    override suspend fun addRepoSearchResults(results : List<GitRepo>, query:String){
            withContext(Dispatchers.IO) {
                gitRepoDAO.deleteAll()
                gitRepoDAO.insertAll(results.map { it.toGitRepoEntity(query) })
            }
    }

    /**
     * Returns all of the Repos in the DB as a List of GitRepo Objects
     */
    override suspend fun getAllRepos () : List<GitRepo>{
        return gitRepoDAO.getAll().map { it.toGitRepo() }
    }

}