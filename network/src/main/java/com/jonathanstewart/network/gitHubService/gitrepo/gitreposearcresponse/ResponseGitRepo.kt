package com.jonathanstewart.network.gitHubService.gitrepo.gitreposearcresponse

import com.jonathanstewart.data.GitRepo

/**
 * Data class for unmarshalling git hub repo data
 * this class is internal because all interactions with the network should be preformed through the exposed network services
 */

internal data class ResponseGitRepo(
    val `private`: Boolean,
    val archive_url: String,
    val archived: Boolean,
    val assignees_url: String,
    val blobs_url: String,
    val branches_url: String,
    val clone_url: String,
    val collaborators_url: String,
    val comments_url: String,
    val commits_url: String,
    val compare_url: String,
    val contents_url: String,
    val contributors_url: String,
    val created_at: String,
    val default_branch: String,
    val deployments_url: String,
    val description: String,
    val disabled: Boolean,
    val downloads_url: String,
    val events_url: String,
    val fork: Boolean,
    val forks: Int,
    val forks_count: Int,
    val forks_url: String,
    val full_name: String,
    val git_commits_url: String,
    val git_refs_url: String,
    val git_tags_url: String,
    val git_url: String,
    val has_downloads: Boolean,
    val has_issues: Boolean,
    val has_pages: Boolean,
    val has_projects: Boolean,
    val has_wiki: Boolean,
    val homepage: String,
    val hooks_url: String,
    val html_url: String,
    val id: Int,
    val issue_comment_url: String,
    val issue_events_url: String,
    val issues_url: String,
    val keys_url: String,
    val labels_url: String,
    val language: String,
    val languages_url: String,
    val license: Any,
    val merges_url: String,
    val milestones_url: String,
    val mirror_url: Any,
    val name: String,
    val node_id: String,
    val notifications_url: String,
    val open_issues: Int,
    val open_issues_count: Int,
    val owner: ResponseGitRepoOwner,
    val pulls_url: String,
    val pushed_at: String,
    val releases_url: String,
    val score: Double,
    val size: Int,
    val ssh_url: String,
    val stargazers_count: Int,
    val stargazers_url: String,
    val statuses_url: String,
    val subscribers_url: String,
    val subscription_url: String,
    val svn_url: String,
    val tags_url: String,
    val teams_url: String,
    val trees_url: String,
    val updated_at: String,
    val url: String,
    val watchers: Int,
    val watchers_count: Int
)

/**
 * Extension function that converts the response object into the exposed data object
 */
internal fun ResponseGitRepo.toGitRepo() : GitRepo{
    return GitRepo(
        repoName = name,
        repoDescription = description,
        stars = stargazers_count,
        language = language,
        forks = forks,
        size = size,
        url = html_url,
        issues = open_issues_count,
        id = id,
        owner = owner.toOwner()
    )
}

