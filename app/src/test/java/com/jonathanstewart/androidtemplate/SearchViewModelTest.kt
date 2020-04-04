package com.jonathanstewart.androidtemplate


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.jonathanstewart.data.GitRepo
import com.jonathanstewart.data.Owner
import com.jonathanstewart.repository.CoroutineContextProvider
import com.jonathanstewart.repository.GitHubDataRepository
import com.jonathanstewart.repository.Resource
import com.jonathanstewart.androidtemplate.search.SearchViewModelImpl
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class SearchViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    val mockGitHubDataRepository : GitHubDataRepository = mockk(relaxed = true)
    val testQuery = "TESTQUERY"
    val testRepos by lazy{generateTestRepos()}


    val searchViewwModel by lazy {SearchViewModelImpl(mockGitHubDataRepository)}
    val latestQuery = MutableLiveData<String>()
    val searchResults = MutableLiveData<Resource<List<GitRepo>>>()

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        CoroutineContextProvider.setTestContexts(true)
        every { mockGitHubDataRepository.getLatestQuery() } returns latestQuery
        coEvery { mockGitHubDataRepository.repoSearch(any()) } returns searchResults
    }

    @Test
    fun testLatestQueryLiveData() {
        latestQuery.postValue(testQuery)
        assertEquals(searchViewwModel.latestQuery.value, testQuery)
    }

    @Test
    fun testSearchSuccess() {
        searchViewwModel.repoSearchResults.observeForever{}
        searchViewwModel.searchForRepos(testQuery)
        searchResults.postValue(Resource.success(testRepos))
        assertEquals(testRepos, searchViewwModel.repoSearchResults.value)
    }

    @Test
    fun testSearchErrorNullData() {
        val testException = Exception("TESTException")
        searchViewwModel.repoSearchResults.observeForever{}
        searchViewwModel.errors.observeForever{}
        searchViewwModel.searchForRepos(testQuery)
        searchResults.postValue(Resource.error(testException, null))
        assertEquals(null, searchViewwModel.repoSearchResults.value)
        assertEquals(testException, searchViewwModel.errors.value)
    }

    @Test
    fun testSearchLoadingNullData() {
        searchViewwModel.isLoadingRepos.observeForever{}
        searchViewwModel.repoSearchResults.observeForever{}
        searchViewwModel.errors.observeForever{}
        searchViewwModel.searchForRepos(testQuery)
        searchResults.postValue(Resource.loading(null))
        assertEquals(null, searchViewwModel.repoSearchResults.value)
        assertEquals(null, searchViewwModel.errors.value)
        assertEquals(Resource.Status.LOADING, searchViewwModel.isLoadingRepos.value)
    }


    fun generateTestRepos(): ArrayList<GitRepo> {
        val testRepos = ArrayList<GitRepo>()
        for (i in 0..10){
            testRepos.add(
                GitRepo(
                    repoName = "TestName" + i,
                    repoDescription = "TestDesc" + i,
                    stars = i*13,
                    forks = i*9,
                    issues = i*6,
                    size = i*124,
                    query = "TestQuery"+i,
                    url = "TestUrl"+i,
                    owner = Owner(name = "TestOwnerName"+i, avatarUrl = "TestAvatarUrl"+i, id = i+1000,url = "TestURL" +i),
                    id = i,
                    language = "TestLanguage"+i
                )
            )
        }
        return testRepos
    }

}