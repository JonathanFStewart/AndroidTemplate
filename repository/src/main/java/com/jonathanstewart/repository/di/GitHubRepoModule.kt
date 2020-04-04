package com.jonathanstewart.repository.di

import com.jonathanstewart.repository.GitHubDataRepository
import com.jonathanstewart.repository.GitHubDataRepositoryImpl
import org.koin.dsl.module

/**
 * Koin Module for the Repository Module
 */
val githubRepoModule = module {

    single { GitHubDataRepositoryImpl(get(),get()) as GitHubDataRepository }

}