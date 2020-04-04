package com.jonathanstewart.androidtemplate.search

import androidx.lifecycle.*
import com.jonathanstewart.data.GitRepo
import com.jonathanstewart.repository.CoroutineContextProvider
import com.jonathanstewart.repository.GitHubDataRepository
import com.jonathanstewart.repository.Resource
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class SearchViewModel: ViewModel() {
    abstract val latestQuery: LiveData<String?>
    abstract val repoSearchResults: MediatorLiveData<List<GitRepo>?>
    abstract val isLoadingRepos: LiveData<Resource.Status>
    abstract val errors: LiveData<Throwable>
    abstract fun searchForRepos (query:String)
}

/**
 * Handles data interactions for the Search Fragment and exposes these four live data objects to the view
 * @latestQuery
 * @repoSearchResults for a successful acquisitions git hub repository  data
 * @isLoadingRepos for the showing the loading status of the git hub repository transaction
 * @errors for exposing data acquisition errors
 *
 * @param gitHubDataRepository handles acquiring data from the network and local storage
 */
class SearchViewModelImpl (private val gitHubDataRepository: GitHubDataRepository): SearchViewModel() {

    override val latestQuery = gitHubDataRepository.getLatestQuery()

    private var reposSource: LiveData<Resource<List<GitRepo>>> = MutableLiveData()
    override val repoSearchResults = MediatorLiveData<List<GitRepo>?>()
    private val _isReposLoading = MutableLiveData<Resource.Status>()
    override val isLoadingRepos: LiveData<Resource.Status> get() = _isReposLoading
    private val _errors = MutableLiveData<Throwable>()
    override val errors: LiveData<Throwable> get() = _errors
    /**
     * Calls the necessary repository methods to get github repositorys for the given query
     * Splits the response from the repository into three live datas presented to the view
     * @repoSearchResults for a successful acquisition of the data
     * @isLoadingRepos for the showing the loading status
     * @errors for handling errors
     *
     *@param query is the string query used to search for repositories
     */
    override fun searchForRepos (query:String) {
         viewModelScope.launch {
             repoSearchResults.removeSource(reposSource)
             withContext(CoroutineContextProvider.IO) { reposSource = gitHubDataRepository.repoSearch(query) }
             repoSearchResults.addSource(reposSource) {
                 if (it.status == Resource.Status.SUCCESS) repoSearchResults.value = it.data
                 _isReposLoading.value = it.status
                 if (it.status == Resource.Status.ERROR) _errors.value = it.error
             }
         }
     }
}