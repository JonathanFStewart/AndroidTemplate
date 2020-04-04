package com.jonathanstewart.androidtemplate.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jonathanstewart.data.GitRepo
import com.jonathanstewart.repository.Resource
import com.jonathanstewart.androidtemplate.R
import com.jonathanstewart.androidtemplate.repodetails.DetailsActivity
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Fragment for displaying the results of a repo query
 */
class SearchFragment : Fragment () {

    var snackbar : Snackbar? = null

    val searchViewModel : SearchViewModel by viewModel()

    var lastQuery : String? = null

    val searchResultsRecyclerAdapter: SearchResultsRecyclerAdapter by lazy {
        SearchResultsRecyclerAdapter(context) { gitRepo : GitRepo -> repoItemClicked(gitRepo)}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        search_results_recycler_view.layoutManager = LinearLayoutManager(context)
        search_results_recycler_view.adapter = searchResultsRecyclerAdapter

        observerLatestQuery()
        observeErrors()
        observeSearchResults()
        observeSearchResultsLoading()
    }

    /**
     * Observers the latest query live data to fetch new repos if necessary
     */
    private fun observerLatestQuery () {
        searchViewModel.latestQuery.observe(this, Observer {
            it?.let{
                //this checks to make sure we don't reload data if the view was not taken out of memory
                if (!it.equals(lastQuery)) {
                    lastQuery  = it
                    searchViewModel.searchForRepos(it)
                }
            }
        })
    }

    /**
     * Universal error handeling for this fragment. All errors that come through the on error live data will be processed
     * here. The implementation of this function in this case to display a snackbar with the error message
     */
    private fun observeErrors () {
        //observe errors, all repository errors should come through this live data
        searchViewModel.errors.observe(this, Observer {
            Snackbar.make(searchFragmentMainView, it.message.toString(), Snackbar.LENGTH_SHORT).show()
        })
    }

    /**
     * Observes for Github Repository results from the repository class and pushes the dat to the recycler view adapter
     */
    private fun observeSearchResults () {
        //observer results and load them into the recycler adapter
        searchViewModel.repoSearchResults.observe(this, Observer {

            if (it.isNullOrEmpty()){
                snackbar = Snackbar.make(search_results_recycler_view, "No Results", Snackbar.LENGTH_SHORT)
                snackbar?.show()
                searchResultsRecyclerAdapter.items = ArrayList()

            } else {
                searchResultsRecyclerAdapter.items = ArrayList(it)
            }
            searchResultsRecyclerAdapter.notifyDataSetChanged()

        })
    }

    /**
     * Observes the Loading Live data and presents a loading spinner when necessary
     */
    private fun observeSearchResultsLoading(){
        //shows spinner while we are waiting for results
        searchViewModel.isLoadingRepos.observe(this, Observer {
            if (it == Resource.Status.LOADING) {
                loadingSpinner.visibility = View.VISIBLE
                search_results_recycler_view.visibility = View.GONE
            }
            else {
                loadingSpinner.visibility = View.GONE
                search_results_recycler_view.visibility = View.VISIBLE
            }
        })
    }

    /**
     * Starts the repo detail activity when a repository cell is clicked
     */
    private fun repoItemClicked(gitRepo: GitRepo) {
        val intent = Intent(context, DetailsActivity::class.java)
        intent.putExtra("repo", gitRepo)
        startActivity(intent)
    }
}