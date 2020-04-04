package com.jonathanstewart.androidtemplate.about


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jonathanstewart.androidtemplate.R
import kotlinx.android.synthetic.main.dependency_item_layout.view.*

class DependencyRecyclerAdapter(val context: Context?, val clickListener: (Dependency) -> Unit) : RecyclerView.Adapter<DependencyRecyclerAdapter.DependencyItemHolder>() {

    var items = dependencyList.list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DependencyItemHolder {
        return DependencyItemHolder(LayoutInflater.from(context).inflate(R.layout.dependency_item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(repoItemHolder:DependencyItemHolder , position: Int) {
        repoItemHolder.bind(items[position], clickListener)
    }

    class DependencyItemHolder (val listItemView: View ) : RecyclerView.ViewHolder(listItemView) {
        fun bind(dependency: Dependency, clickListener: (Dependency) -> Unit) {
            with(listItemView) {
                dependency_title.text = dependency.name
                dependency_description.text = dependency.descripton
                setOnClickListener { clickListener(dependency) }

            }
        }
    }
}