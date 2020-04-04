package com.jonathanstewart.repository

import androidx.lifecycle.LiveData
import com.jonathanstewart.data.GitRepo
import com.jonathanstewart.network.gitHubService.GitRepoNetworkService
import com.jonathanstewart.persistence.GitRepoPersistenceService

interface GitHubDataRepository{
    suspend fun repoSearch(query:String) : LiveData<Resource<List<GitRepo>>>
    fun getQueries(): LiveData<List<String>>
    fun getLatestQuery(): LiveData<String?>
    suspend fun newQuery(query: String)
}

/**
 * Class that manages Local and Remote data sources to provide data seamlessly from either source
 * @param gitRepoNetworkService The Network data source for the Github API
 * @param gitRepoPersistenceService The Persistence Data Source for the Github API
 */
class GitHubDataRepositoryImpl (
    private val gitRepoNetworkService: GitRepoNetworkService,
    private val gitRepoPersistenceService: GitRepoPersistenceService
) : GitHubDataRepository {

    /**
     * Suspend function that gets a list of Repositories for the given query from.
     * This function uses the the NetworkBoundResource Abstract class which handles
     * returning data from the network, storing the result in the DB and a then
     * providing the result from the DB. This is done this way so that we maintain the single source
     * of truth principle by ensuring our data is always coming from the Local DB
     */
    override suspend fun repoSearch(query: String) : LiveData<Resource<List<GitRepo>>> {
        return object : NetworkBoundResource<List<GitRepo>>() {
            override suspend fun saveCallResults(items: List<GitRepo>) {
                gitRepoPersistenceService.addRepoSearchResults(items, query)
            }

            override fun shouldFetch(data: List<GitRepo>?): Boolean {
                 data?.getOrNull(0)?.let {
                     return !it.query.equals(query) } ?: return true
            }

            override suspend fun loadFromDb(): List<GitRepo>? {
                return gitRepoPersistenceService.getAllRepos()
            }

            override suspend fun createCallAsync(): List<GitRepo> {
                return gitRepoNetworkService.searchForRepos(query)
            }

        }.build().asLiveData()
    }

    /**
     * Saves a query to the DB
     */
    override suspend fun newQuery(query: String) {
        gitRepoPersistenceService.addQuery(query)
    }

    /**
     * Provides a list of all the queries in the DB
     */
    override fun getQueries() : LiveData<List<String>> {
        return gitRepoPersistenceService.getAllQueries()
    }

    /**
     * Provides the Latest query from the DB
     */
    override fun getLatestQuery(): LiveData<String?> {
        return gitRepoPersistenceService.getLatestQuery()
    }

}