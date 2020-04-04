package com.jonathanstewart.persistence.gitreposearch

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jonathanstewart.data.Owner

/**
 * Data class / Room Entity representing a Git Repo Owner in the persistince module
 * this class is internal as all interactions with the persistence layer should be done through the exposed persistence services
 */
@Entity(tableName = "GitOwners")
internal data class GitOwnerEntity (
    val ownername : String?,
    val ownerurl :String,
    @PrimaryKey
    val ownerid: Int?,
    val avatarUrl: String?
)

/**
 * Internal extension function for converting the Common Data Structure into the persistence entity representation
 */
internal fun Owner.toGitOwnerEntity() : GitOwnerEntity {
    return GitOwnerEntity(
        ownername = name,
        ownerurl = url,
        ownerid = id,
        avatarUrl = avatarUrl
    )
}
/**
 * Internal extension function for converting the persistence entity representation into the Common Data Structure
 */
internal fun GitOwnerEntity.toOwner() : Owner {
    return Owner(
        name = ownername,
        url = ownerurl,
        id = ownerid,
        avatarUrl = avatarUrl
    )
}