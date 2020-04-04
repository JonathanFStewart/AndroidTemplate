package com.jonathanstewart.data

import java.io.Serializable

/**
 * Serializable data class for storing relevant data about a git hib repo
 * @param repoName The name of the repository
 * @param repoDescription A description of the repository
 * @param stars The number of stars (followers) a repo has
 * @param language The primary language the repository is written in
 * @param forks The number of forks a repository has
 * @param size The total size of the repository in kB
 * @param issues The number of active issues in the repo
 * @param url The Url for the repository
 * @param id The id of the repository
 * @param owner Object representing the Owner of the repository
 * @param query The query the user provided that this repository was fetched with. This is stored in the DB and is used
 * to determine if we need to fetch new repositories from the network which is only done when a new query is provided
 */
data class GitRepo(
    var repoName: String,
    val repoDescription: String? ,
    val stars : Int,
    val language: String?,
    val forks: Int,
    val size: Int,
    val issues: Int,
    val url : String,
    val id : Int,
    var owner: Owner ,
    var query: String? = ""

) :Serializable