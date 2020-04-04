package com.jonathanstewart.androidtemplate.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jonathanstewart.data.GitRepo
import com.jonathanstewart.androidtemplate.R
import kotlinx.android.synthetic.main.gitrepo_list_item.view.*

class SearchResultsRecyclerAdapter(val context: Context?, val clickListener: (GitRepo) -> Unit) : RecyclerView.Adapter<SearchResultsRecyclerAdapter.GitRepoItemHolder>() {

    var items = ArrayList<GitRepo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitRepoItemHolder {
        return GitRepoItemHolder(LayoutInflater.from(context).inflate(R.layout.gitrepo_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(repoItemHolder: GitRepoItemHolder, position: Int) {
        repoItemHolder.bind(items[position],clickListener)
    }

    class GitRepoItemHolder (val listItemView: View ) : RecyclerView.ViewHolder(listItemView) {
        fun bind(gitRepo: GitRepo, clickListener: (GitRepo) -> Unit) {
            with(listItemView) {
                repoName.text = gitRepo.repoName
                repoDescription.text = gitRepo.repoDescription
                repoStars.text = gitRepo.stars.toString()
                repoLanguage.text = gitRepo.language ?: resources.getString(R.string.not_available)
                setOnClickListener { clickListener(gitRepo) }
            }
        }
    }
}