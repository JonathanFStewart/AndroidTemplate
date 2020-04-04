package com.jonathanstewart.di

import com.jonathanstewart.androidtemplate.repodetails.DetailsSharedViewModel
import com.jonathanstewart.androidtemplate.repodetails.DetailsSharedViewModelImpl
import com.jonathanstewart.androidtemplate.search.SearchViewModel
import com.jonathanstewart.androidtemplate.search.SearchViewModelImpl
import com.jonathanstewart.androidtemplate.searchbar.SearchBarViewModel
import com.jonathanstewart.androidtemplate.searchbar.SearchBarViewModelImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * Koin Module for Search ViewModels
 */

val gitRepoSearchModule = module {
    viewModel { SearchBarViewModelImpl(get()) as SearchBarViewModel }
    viewModel { SearchViewModelImpl(get()) as SearchViewModel }
    viewModel { DetailsSharedViewModelImpl() as DetailsSharedViewModel }
}