package com.jonathanstewart.network.gitHubService.gitrepo.gitreposearcresponse

import com.jonathanstewart.data.Owner

/**
* Generated Data class for unmarshalling git hub repo owner data
 * this class is internal because all interactions with the network should be preformed through the exposed network services
*/
internal data class ResponseGitRepoOwner(
    val avatar_url: String,
    val events_url: String,
    val followers_url: String,
    val following_url: String,
    val gists_url: String,
    val gravatar_id: String,
    val html_url: String,
    val id: Int,
    val login: String,
    val node_id: String,
    val organizations_url: String,
    val received_events_url: String,
    val repos_url: String,
    val site_admin: Boolean,
    val starred_url: String,
    val subscriptions_url: String,
    val type: String,
    val url: String
)

/**
 * Extension function for converting the response object into the exposed Data Objects
 */
internal fun ResponseGitRepoOwner.toOwner() : Owner {
    return Owner (
        name = login,
        avatarUrl = avatar_url,
        id = id,
        url = html_url
    )
}