package com.jonathanstewart.androidtemplate.repodetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jonathanstewart.data.Owner
import com.jonathanstewart.androidtemplate.R
import kotlinx.android.synthetic.main.owner_detailed_card.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class OwnerDetailsFragment : Fragment() {

    private val sharedViewModel: DetailsSharedViewModel  by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.owner_detailed_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.repoLiveData.observe(this, Observer {
            setupOwnerCard(it.owner)
        })
    }

    /**
     * Populates the fields in the Owner card with the data in
     * @param owner*
     */
    fun setupOwnerCard(owner: Owner){
        owner_name.text = owner.name

        Glide.with(this)
            .load(owner.avatarUrl)
            .apply(RequestOptions.circleCropTransform())
            .placeholder(createProgressDrawable() ?: resources.getDrawable(R.drawable.ic_person_blue_24dp))
            .into(owner_avatar_view)

        view_profile_button.setOnClickListener {openUrlInBrowser(owner.url)}
    }

    fun openUrlInBrowser(url:String){
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

    fun createProgressDrawable() :CircularProgressDrawable? {
       return  context?.let {
            val circularProgressDrawable = CircularProgressDrawable(it)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            circularProgressDrawable
        }
    }
}