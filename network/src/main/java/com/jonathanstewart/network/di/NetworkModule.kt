package com.jonathanstewart.network.di

import com.jonathanstewart.network.gitHubService.GitRepoNetworkService
import com.jonathanstewart.network.gitHubService.GitRepoNetworkServiceImpl
import com.jonathanstewart.network.gitHubService.GithubApiFactory
import org.koin.dsl.module

/**
 * Koin Module for the Network Module
 */
 val networkModule = module {

     single { GitRepoNetworkServiceImpl(get()) as GitRepoNetworkService}
     single { GithubApiFactory.provideGithubApiService() }

}