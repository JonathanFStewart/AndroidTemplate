package com.jonathanstewart.androidtemplate.searchbar

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonathanstewart.repository.GitHubDataRepository
import kotlinx.coroutines.launch

abstract class SearchBarViewModel :ViewModel() {
    abstract val queriesLiveData : LiveData<List<String>>
    abstract val latestQuery : LiveData<String?>
    abstract fun search(query:String)
}

/**
 * ViewModel for the Searchbar Fragment
 * Exposes
 * @queriesLiveData for providing all of the locally stored queries
 * @latestQuery which provides the most recent query
 * @search(query) which begins a search based on the provided query
 *
 * @param githubDataRepository handles the data interaction between local and network sources
 */
class SearchBarViewModelImpl(private val githubDataRepository:GitHubDataRepository) : SearchBarViewModel(){

    override val queriesLiveData : LiveData<List<String>> by lazy{githubDataRepository.getQueries()}
    override val latestQuery : LiveData<String?> by lazy { githubDataRepository.getLatestQuery() }
    /**
     * Launches a query for the given parameter
     * @param query the query parameter to search for
     */
    override fun search(query:String){
        viewModelScope.launch  { githubDataRepository.newQuery(query) }
    }

}