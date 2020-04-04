package com.jonathanstewart.data

import java.io.Serializable

/**
 * Serializable data class storing the relevant data about a repository owner
 * @param name The Username of the Repo Owner
 * @param url The Url of the Owner's github page
 * @param id Unique id assigned github users
 * @param avatarUrl The url of the User's Git Hub Profile Picture
 */

data class Owner (
    val name : String?,
    val url :String,
    val id: Int?,
    val avatarUrl: String?
) :Serializable