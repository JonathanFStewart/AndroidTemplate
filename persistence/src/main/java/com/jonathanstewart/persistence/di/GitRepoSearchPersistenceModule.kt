package com.jonathanstewart.persistence.di

import com.jonathanstewart.persistence.AppDatabase
import com.jonathanstewart.persistence.AppDatabaseFactory
import com.jonathanstewart.persistence.GitRepoPersistenceService
import com.jonathanstewart.persistence.GitRepoPersistenceServiceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Koin module for the persistence module
 */
val gitRepoSearchPersistenceModule = module {

    single {get<AppDatabase>().searchQueryDAO()}

    single {get<AppDatabase>().gitRepoDAO()}

    single {AppDatabaseFactory.provideDatabase(androidContext())}

    single { GitRepoPersistenceServiceImpl(get(), get()) as GitRepoPersistenceService }
}