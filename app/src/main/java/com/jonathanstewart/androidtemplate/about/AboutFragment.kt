package com.jonathanstewart.androidtemplate.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jonathanstewart.androidtemplate.R
import kotlinx.android.synthetic.main.fragment_about.*

/**
 * Fragment for the about screen
 */
class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dependency_recycler_view.layoutManager = LinearLayoutManager(context)
        dependency_recycler_view.adapter = DependencyRecyclerAdapter(context){ dependency : Dependency -> dependencyItemClicked(dependency)}
    }

    private fun dependencyItemClicked(dependency: Dependency) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(dependency.url))
        startActivity(browserIntent)
    }
}