package com.jonathanstewart.androidtemplate.repodetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jonathanstewart.data.GitRepo
import com.jonathanstewart.inTransaction
import com.jonathanstewart.androidtemplate.MainActivity
import com.jonathanstewart.androidtemplate.R
import kotlinx.android.synthetic.main.activity_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Activity that displays the details of a repo
 */
class DetailsActivity : AppCompatActivity() {

    val sharedViewModel : DetailsSharedViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val repo = getIntent()?.getExtras()?.getSerializable("repo") as? GitRepo?

        exit_button.setOnClickListener {
            onBackPressed()
        }
        repo?.let {
            supportFragmentManager.inTransaction {
                add(R.id.repo_detail_container, RepoDetailsFragment())
                add(R.id.owner_detail_container, OwnerDetailsFragment())
            }
            sharedViewModel.repoLiveData.postValue(it)
            open_in_browser_button.setOnClickListener {openUrlInBrowser(repo.url) }
        }
    }

    /**
     * Opens the default device web browser to the url specified in
     * @param url
     */
    fun openUrlInBrowser(url:String){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }


}