package com.jonathanstewart.androidtemplate.searchbarfragment

import android.content.Context
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.jonathanstewart.androidtemplate.R
import com.jonathanstewart.androidtemplate.searchbar.SearchBarFragment
import com.jonathanstewart.androidtemplate.searchbar.SearchBarViewModel
import com.jonathanstewart.androidtemplate.test.base.FragmentTest
import io.mockk.*
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class SearchBarFragmentUiTest : FragmentTest<SearchBarFragment>(SearchBarFragment::class.java) {

    val testSearchBarViewModel : SearchBarViewModel = mockk(relaxed = true)
    val testQueryList = arrayListOf("Test1", "Test2","Test3","Test4","Test5","Test6")
    val searchBarTestModule = module{
        single { testSearchBarViewModel as SearchBarViewModel}
    }

    val quiriesLiveData = MutableLiveData<List<String>>()
    val latestQuery = MutableLiveData<String?>()

    @Before
    override fun setUp() {
        super.setUp()
        stopKoin()
        every { testSearchBarViewModel.queriesLiveData } returns quiriesLiveData
        every { testSearchBarViewModel.latestQuery } returns latestQuery
        every { testSearchBarViewModel.search(any()) } just runs

        startKoin {
            modules(searchBarTestModule)
        }

        startFragment()
    }

    @After
    fun after(){
        stopKoin()
    }

    @Test
    fun checkViews() {
        onView(withId(R.id.search_box_text_view)).check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.searchIcon)).check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun checkAutoComplete() {
        quiriesLiveData.postValue(testQueryList)
        for (query in testQueryList){
            checkAutoCompleteListVisible()
            onData(equalTo(query)).inRoot(RootMatchers.isPlatformPopup()).perform(click())
            onView(withId(R.id.search_box_text_view)).check(matches(withText(query)))
            onView(withId(R.id.search_box_text_view)).perform(replaceText(""))
        }

    }

    fun checkAutoCompleteListVisible() {
        onView(withId(R.id.search_box_text_view)).perform(typeText("tes"))
        for (query in testQueryList){
            onData(equalTo(query)).inRoot(RootMatchers.isPlatformPopup()).check(matches(ViewMatchers.isDisplayed()))
        }
    }

    @Test
    fun checkLatestQueryPopulates() {
        for (query in testQueryList) {
            latestQuery.postValue(query)
            onView(withId(R.id.search_box_text_view)).check(matches(withText(query)))
        }

    }

    fun isKeyboardShown(): Boolean {
        val inputMethodManager = InstrumentationRegistry.getInstrumentation().targetContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.isAcceptingText
    }

    @Test
    fun submitQuery() {
        for (query in testQueryList) {
            onView(withId(R.id.search_box_text_view)).perform(typeText(query))
            onView(withId(R.id.search_box_text_view))
                .check(matches(hasImeAction(EditorInfo.IME_ACTION_SEARCH)))
                .perform(pressImeActionButton())
            verify { testSearchBarViewModel.search(query)}
            assert(!isKeyboardShown())
            onView(withId(R.id.search_box_text_view)).perform(replaceText(""))
        }
    }
}