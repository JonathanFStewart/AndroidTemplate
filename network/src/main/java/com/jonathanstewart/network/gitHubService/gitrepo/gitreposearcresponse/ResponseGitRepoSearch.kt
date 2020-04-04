package com.jonathanstewart.network.gitHubService.gitrepo.gitreposearcresponse

/**
 * Generated Data class for unmarshalling git hub repo response data
 * this class is internal because all interactions with the network should be preformed through the exposed network services
 */
internal data class ResponseGitRepoSearch(
    val incomplete_results: Boolean,
    val items: List<ResponseGitRepo>,
    val total_count: Int
)