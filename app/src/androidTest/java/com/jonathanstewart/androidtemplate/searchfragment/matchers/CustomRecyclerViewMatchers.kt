package com.jonathanstewart.androidtemplate.matchers

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import com.jonathanstewart.androidtemplate.search.SearchResultsRecyclerAdapter
import kotlinx.android.synthetic.main.gitrepo_list_item.view.*
import org.hamcrest.Description
import org.hamcrest.Matcher

class CustomRecyclerViewMatchers {
    companion object {
        fun withRepoName(repoName: String): Matcher<RecyclerView.ViewHolder> {
            return object : BoundedMatcher<RecyclerView.ViewHolder, SearchResultsRecyclerAdapter.GitRepoItemHolder>(
                SearchResultsRecyclerAdapter.GitRepoItemHolder::class.java) {
                override fun describeTo(description: Description?) {
                    description?.appendText("RecyclerView with item repoName: $repoName")
                }

                override fun matchesSafely(item: SearchResultsRecyclerAdapter.GitRepoItemHolder?): Boolean {
                    return item?.let{it.listItemView.repoName.text.toString().equals(repoName)} ?: false
                }
            }
        }
    }
}