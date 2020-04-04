package com.jonathanstewart.androidtemplate.searchbar

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_search_bar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Fragment that presents a search box for the user to enter queries
 */
class SearchBarFragment : Fragment() {

    private val searchBarViewModel : SearchBarViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(com.jonathanstewart.androidtemplate.R.layout.fragment_search_bar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchListener()
        observeQueriesList()
        observeLatestQuery()
    }

    /**
     * Observes the quieries list saved in the local DB and presents that list as a search history
     * on the autocomplete text bos
     */
    fun observeQueriesList(){
        searchBarViewModel.queriesLiveData.observe(this, Observer {
            val adapter = ArrayAdapter<String>(
                context, // Context
                android.R.layout.simple_dropdown_item_1line, // Layout
                it // Array
            )
            search_box_text_view.setAdapter(adapter)
        })
    }

    /**
     * Ensures the latest query is populated in the text view on fragment start
     */
    fun observeLatestQuery() {
        searchBarViewModel.latestQuery.observe(this, Observer {
            it?.let{
                search_box_text_view.setText(it)
            }
        })
    }

    /**
     * Sets up the IME listener for the keyboard on the Search box, and performs a query when the IME_ACTION_SEARCH
     * action is performed
     */
    fun setupSearchListener() {
        search_box_text_view.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                preformSearch(search_box_text_view.text.toString())
                true
            } else {
                false
            }
        }
    }

    /**
     * Hides the Keyboard
     * @param view The view that currently has focus and is showing the keyboard
     */
    fun hideKeyboard(view : View) {
        val inputMethodManager =  (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /**
     * Send the query to the viewmodel to begin getting new repositories
     * @param queryText is the query the user provided
     */
    fun preformSearch(queryText: String){
        search_box_text_view.clearFocus()
        hideKeyboard(search_box_text_view)
        if (queryText.isNotBlank()) {
            searchBarViewModel.search(queryText)
        } else {
            Snackbar.make(search_box_text_view,"Query must not be empty", Snackbar.LENGTH_SHORT).show()
        }
    }
}