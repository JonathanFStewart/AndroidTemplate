package com.jonathanstewart.network.gitHubService

import com.jonathanstewart.network.gitHubService.gitrepo.gitreposearcresponse.ResponseGitRepoSearch
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Contains all of the retrofit API calls for the GithubAPI
 */
internal interface GithubApiService {

    /**
     * Calls the Git Hub Repository search api
     * @param queryText non option string containing the text the user wishes to search for
     * @param sortBy optional param for what metric to sort the Repositories by, Defaults to stars
     * @param sortOrder optional param for sorting by 'asce' ascending or descending 'desc' on the metric given
     * in sortBy. Defaults to 'desc' for Descending
     * @param itemsPerPage optional param for setting how many items will appear on each page. Defaults to 20
     */
    @GET("/search/repositories")
    fun searchRepositories(
        @Query("q") queryText: String,
        @Query("sort") sortBy: String? = "stars",
        @Query("order") sortOrder: String? = "desc",
        @Query("per_page") itemsPerPage: Int? = 20
        ) : Deferred<ResponseGitRepoSearch>
}