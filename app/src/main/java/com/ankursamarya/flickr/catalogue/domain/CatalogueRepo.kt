package com.ankursamarya.flickr.catalogue.domain

import FlickrDataSourceFactory
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.switchMap
import androidx.paging.Config
import androidx.paging.LivePagedListBuilder
import androidx.paging.toLiveData
import com.ankursamarya.flickr.catalogue.data.FlickrApi
import com.ankursamarya.flickr.catalogue.data.Image
import com.ankursamarya.flickr.catalogue.ui.Listing
import java.util.concurrent.Executors

class CatalogueRepo(val apiService: FlickrApi, val lifecycleOwner: LifecycleOwner) {

    fun fetchData(searchedText: String): Listing<Image> {
        val sourceFactory = FlickrDataSourceFactory(apiService, searchedText, lifecycleOwner)

        val livePagedList =  LivePagedListBuilder(sourceFactory, Config(10, 5, false, 30))
            .setInitialLoadKey(0)
            .setBoundaryCallback(null)
            .setFetchExecutor( Executors.newSingleThreadExecutor())
            .build()

        return Listing(
            pagedList = livePagedList,
            networkState = sourceFactory.sourceLiveData.switchMap {
                it.networkState
            },
            refresh = {
                sourceFactory.sourceLiveData.value?.invalidate()
            },
            refreshState = sourceFactory.sourceLiveData.switchMap {
                it.initialLoad
            },
            retry = {
                sourceFactory.sourceLiveData.value?.retryAllFailed()
            }
        )
    }


}