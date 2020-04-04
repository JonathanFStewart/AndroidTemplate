package com.jonathanstewart.persistence.gitreposearch

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jonathanstewart.data.GitRepo

/**
 * Data class / Room Entity representing a Git Repo in the persistince module
 * this class is internal as all interactions with the persistence layer should be done through the exposed persistence services
 */
@Entity(
    tableName = "GitRepos")
internal data class GitRepoEntity(
    val repoName: String,
    val query:String,
    val repoDescription: String,
    val stars : Int,
    val language: String,
    val forks: Int,
    val size: Int,
    val issues: Int,
    val url : String,
    @PrimaryKey
    val id : Int,
    @Embedded
    val owner : GitOwnerEntity
)

/**
 * Internal extension function for converting the Common Data Structure into the persistence entity representation
 */
internal fun GitRepo.toGitRepoEntity(query:String) : GitRepoEntity {
    return GitRepoEntity(
        repoName = repoName,
        repoDescription = repoDescription?:"N/A",
        stars = stars,
        language = language?:"N/A",
        forks = forks,
        size = size,
        url = url,
        issues = issues,
        id = id,
        owner = owner.toGitOwnerEntity(),
        query = query
    )
}
/**
 * Internal extension function for converting the persistence entity representation into the Common Data Structure
 */
internal fun GitRepoEntity.toGitRepo() : GitRepo {
    return GitRepo(
        repoName = repoName,
        repoDescription = repoDescription,
        stars = stars,
        language = language,
        forks = forks,
        size = size,
        url = url,
        issues = issues,
        id = id,
        query = query,
        owner = owner.toOwner()
    )
}