package com.jonathanstewart.androidtemplate.repodetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jonathanstewart.data.GitRepo

abstract class DetailsSharedViewModel :ViewModel() {
    abstract val repoLiveData : MutableLiveData<GitRepo>
}

class DetailsSharedViewModelImpl : DetailsSharedViewModel() {
    override val repoLiveData = MutableLiveData<GitRepo>()
}