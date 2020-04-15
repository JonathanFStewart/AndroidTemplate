package com.jonathanstewart.androidtemplate.searchfragment

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.jonathanstewart.data.GitRepo
import com.jonathanstewart.data.Owner
import com.jonathanstewart.repository.Resource
import com.jonathanstewart.androidtemplate.R
import com.jonathanstewart.androidtemplate.matchers.CustomRecyclerViewMatchers
import com.jonathanstewart.androidtemplate.repodetails.DetailsSharedViewModel
import com.jonathanstewart.androidtemplate.search.SearchFragment
import com.jonathanstewart.androidtemplate.search.SearchViewModel
import com.jonathanstewart.androidtemplate.searchbar.SearchBarViewModel
import com.jonathanstewart.androidtemplate.test.base.FragmentTest
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module


class SearchBarFragmentUiTest : FragmentTest<SearchFragment>(SearchFragment::class.java) {

    val testSearchViewModel: SearchViewModel = mockk(relaxed = true)
    val testSearchBarViewModel : SearchBarViewModel = mockk(relaxed = true)
    val testSharedViewModel: DetailsSharedViewModel = mockk(relaxed = true)
    val testRepos :ArrayList<GitRepo> by lazy { generateTestRepos() }

    val searchModule = module {
        single { testSearchViewModel }
        single { testSearchBarViewModel }
        single { testSharedViewModel }
    }

    val latestQuery = MutableLiveData<String?>()
    val repoSearchResults = MediatorLiveData<List<GitRepo>?>()
    val isLoadingRepos = MutableLiveData<Resource.Status>()
    val errors = MutableLiveData<Throwable>()
    val sharedViewModelLiveData = MutableLiveData<GitRepo>()

    @Before
    override fun setUp() {
        super.setUp()
        stopKoin()
        every { testSharedViewModel.repoLiveData } returns sharedViewModelLiveData
        every { testSearchViewModel.latestQuery } returns latestQuery
        every { testSearchViewModel.repoSearchResults } returns repoSearchResults
        every { testSearchViewModel.isLoadingRepos } returns isLoadingRepos
        every { testSearchViewModel.errors } returns errors
        every { testSearchViewModel.searchForRepos(any()) } just runs

        startKoin {
            modules(searchModule)
        }

        startFragment()
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
                ))
        }
        return testRepos
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun checkViews() {
        onView(withId(R.id.search_results_recycler_view))
            .check(matches(isDisplayed()))
    }

    @Test
    fun checkRecyclerViewItems() {
        repoSearchResults.postValue(testRepos)
        val repo = testRepos[0]
        for (repo in testRepos){
            onView(withId(R.id.search_results_recycler_view))
                .perform(RecyclerViewActions.scrollToHolder(CustomRecyclerViewMatchers.withRepoName(repo.repoName)))
            onView(withText(repo.repoName))
                .check(matches(isDisplayed()))
            onView(withText(repo.repoDescription))
                .check(matches(isDisplayed()))
            onView(withText(repo.stars.toString()))
                .check(matches(isDisplayed()))
            onView(withText(repo.language.toString()))
                .check(matches(isDisplayed()))

        }
    }

    @Test
    fun checkDetailsScreens() {
        repoSearchResults.postValue(testRepos)
        for (repo in testRepos){
            onView(withId(R.id.search_results_recycler_view))
                .perform(RecyclerViewActions.scrollToHolder(CustomRecyclerViewMatchers.withRepoName(repo.repoName)))
            onView(withId(R.id.search_results_recycler_view))
                .perform(RecyclerViewActions.actionOnHolderItem(CustomRecyclerViewMatchers.withRepoName(repo.repoName),click()))
            onView(withId(R.id.exit_button)).check(matches(isDisplayed()))
            onView(withId(R.id.open_in_browser_button)).check(matches(isDisplayed()))
            onView(withId(R.id.repoCardView)).check(matches(isDisplayed()))

            onView(withId(R.id.repoName)).check(matches(isDisplayed()))
            onView(withId(R.id.repoName)).check(matches(withText(repo.repoName)))

            onView(withId(R.id.repoDescription)).check(matches(isDisplayed()))
            onView(withId(R.id.repoDescription)).check(matches(withText(repo.repoDescription)))

            onView(withId(R.id.repo_forks)).check(matches(isDisplayed()))
            onView(withId(R.id.repo_forks)).check(matches(withText(repo.forks.toString())))

            onView(withId(R.id.repo_issues)).check(matches(isDisplayed()))
            onView(withId(R.id.repo_issues)).check(matches(withText(repo.issues.toString())))

            onView(withId(R.id.repo_language)).check(matches(isDisplayed()))
            onView(withId(R.id.repo_language)).check(matches(withText(repo.language)))

            onView(withId(R.id.repo_size)).check(matches(isDisplayed()))
            onView(withId(R.id.repo_size)).check(matches(withText(repo.size.toString())))

            onView(withId(R.id.repo_url)).check(matches(isDisplayed()))
            onView(withId(R.id.repo_url)).check(matches(withText(repo.url)))

            onView(withId(R.id.repoStars)).check(matches(isDisplayed()))
            onView(withId(R.id.repoStars)).check(matches(withText(repo.stars.toString())))

            onView(withId(R.id.owner_name)).check(matches(isDisplayed()))
            onView(withId(R.id.owner_name)).check(matches(withText(repo.owner.name)))

            onView(withId(R.id.exit_button)).perform(click())
        }
    }
    
    @Test
    fun loadingLiveDataShowsSpinner() {
        onView(withId(R.id.search_results_recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.loadingSpinner)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        isLoadingRepos.postValue(Resource.Status.LOADING)
        onView(withId(R.id.search_results_recycler_view)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.loadingSpinner)).check(matches(isDisplayed()))
        isLoadingRepos.postValue(Resource.Status.SUCCESS)
        onView(withId(R.id.search_results_recycler_view)).check(matches(isDisplayed()))
        onView(withId(R.id.loadingSpinner)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun errorShowsSnackBar() {
        errors.postValue(Exception("TEST EXCEPTION"))
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText("TEST EXCEPTION")))
    }

    @Test
    fun emptyResulstShowsNoResultsSnackBar() {
        repoSearchResults.postValue(ArrayList<GitRepo>())
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText("No Results")))
    }

    @Test
    fun nullResulstShowsNoResultsSnackBar() {
        repoSearchResults.postValue(null)
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches(withText("No Results")))
    }

}