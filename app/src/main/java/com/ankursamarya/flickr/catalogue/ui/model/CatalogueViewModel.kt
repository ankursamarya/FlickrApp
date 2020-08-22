package com.ankursamarya.flickr.catalogue.ui.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.ankursamarya.flickr.catalogue.data.Image
import com.ankursamarya.flickr.catalogue.domain.CatalogueRepo
import com.ankursamarya.flickr.catalogue.ui.Listing

class CatalogueViewModel(val repo: CatalogueRepo) : ViewModel(){


    private val repoResult = MutableLiveData<Listing<Image>>()


    fun fetchData(searchedText: String){
        repoResult.postValue(repo.fetchData(searchedText))
    }

    val posts =  repoResult.switchMap { it.pagedList }

    val networkState = repoResult.switchMap { it.networkState }
    val refreshState = repoResult.switchMap { it.refreshState }

    fun refresh() {
        repoResult.value?.refresh?.invoke()
    }

    fun retry() {
        repoResult.value?.retry?.invoke()
    }

}