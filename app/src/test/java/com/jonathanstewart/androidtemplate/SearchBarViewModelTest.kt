package com.jonathanstewart.androidtemplate

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.jonathanstewart.repository.GitHubDataRepository
import com.jonathanstewart.androidtemplate.searchbar.SearchBarViewModelImpl
import io.mockk.*
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class SearchBarViewModelTest {

        @get:Rule
        var rule: TestRule = InstantTaskExecutorRule()

        val mockGitHubDataRepository : GitHubDataRepository = mockk()

        val searchBarViewModel = SearchBarViewModelImpl(mockGitHubDataRepository)
        val latestQuery = MutableLiveData<String>()
        val queiriesLiveData = MutableLiveData<List<String>>()
        val testQueryList by lazy {generateTestQueries()}

        fun generateTestQueries(): ArrayList<String> {
                val testRepos = ArrayList<String>()
                for (i in 0..10){
                        testRepos.add("TESTQUERY" + i)
                }
                return testRepos
        }

        @Before
        fun setup() {
                Dispatchers.setMain(Dispatchers.Unconfined)
                every { mockGitHubDataRepository.getLatestQuery() } returns latestQuery
                every { mockGitHubDataRepository.getQueries() } returns queiriesLiveData
                coEvery { mockGitHubDataRepository.newQuery(any()) } just runs
        }

        @Test
        fun testLatestQueryLiveData() {
                latestQuery.postValue("TESTString")
                assertEquals(searchBarViewModel.latestQuery.value, "TESTString")
        }

        @Test
        fun testQueriesLiveData() {
                queiriesLiveData.postValue(testQueryList)
                val result = searchBarViewModel.queriesLiveData.value
                assertTrue(testQueryList.size == result?.size)
                for (s in testQueryList){
                        assertTrue(result!!.contains(s))
                }
        }

        @Test
        fun testSearchFun() {
            searchBarViewModel.search("TESTQUERY")
            coVerify (exactly = 1){ mockGitHubDataRepository.newQuery("TESTQUERY") }
        }

}