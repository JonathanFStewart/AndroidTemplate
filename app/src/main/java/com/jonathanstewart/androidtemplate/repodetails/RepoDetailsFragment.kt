package com.jonathanstewart.androidtemplate.repodetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.jonathanstewart.data.GitRepo
import com.jonathanstewart.androidtemplate.R
import kotlinx.android.synthetic.main.repo_detailed_card.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RepoDetailsFragment : Fragment() {

    private val sharedViewModel: DetailsSharedViewModel  by sharedViewModel()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.repo_detailed_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.repoLiveData.observe(this, Observer {
            populateRepoCard(it)
        })
    }

    fun populateRepoCard(gitRepo: GitRepo){
        repoName.text = gitRepo.repoName
        repoDescription.text = gitRepo.repoDescription
        repoStars.text = gitRepo.stars.toString()
        repo_forks.text = gitRepo.forks.toString()
        repo_issues.text = gitRepo.issues.toString()
        repo_size.text = gitRepo.size.toString()
        repo_language.text = gitRepo.language
        repo_url.text = gitRepo.url
        repoCardView.setOnClickListener(){
            openUrlInBrowser(gitRepo.url )
        }
    }

    fun openUrlInBrowser(url:String){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }
}