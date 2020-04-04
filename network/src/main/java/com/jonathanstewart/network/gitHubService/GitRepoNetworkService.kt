package com.jonathanstewart.network.gitHubService

import com.jonathanstewart.data.GitRepo
import com.jonathanstewart.network.gitHubService.gitrepo.gitreposearcresponse.toGitRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


interface GitRepoNetworkService {
    suspend fun searchForRepos(query: String) : List<GitRepo>
}

/**
 * Exposed service to preform network calls to the github api, and handles type conversion from the Common Data representation
 * to the module specific representation
 * @param githubApiService is the retrofit service
 */
class GitRepoNetworkServiceImpl
    internal constructor(private val githubApiService: GithubApiService)
    : GitRepoNetworkService {
    /**
     * Suspend function that preforms the network request to the github api for the repos with the given query
     * and converts the response object to the Common data representation
     * @param query is the string to preform the search with
     */
    override suspend fun searchForRepos(query: String) : List<GitRepo>  {
        return withContext(Dispatchers.IO) {
            githubApiService.searchRepositories(queryText = query)
                .await().items.map { it.toGitRepo() }
        }
    }
}