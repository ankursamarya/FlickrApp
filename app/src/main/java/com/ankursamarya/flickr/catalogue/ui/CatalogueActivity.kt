package com.ankursamarya.flickr.catalogue.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ankursamarya.flickr.R
import com.ankursamarya.flickr.catalogue.data.FlickrApi
import com.ankursamarya.flickr.catalogue.domain.CatalogueRepo
import com.ankursamarya.flickr.catalogue.ui.adaptor.ImagesAdapter
import com.ankursamarya.flickr.catalogue.ui.model.CatalogueViewModel
import com.ankursamarya.flickr.databinding.ActivityCatalogueBinding
import com.ankursamarya.flickr.network.NetworkingProvider
import com.ankursamarya.flickr.view.SpacesItemDecoration

class CatalogueActivity : AppCompatActivity() {

    companion object {
        const val COLUMNS = 3
    }

    private lateinit var binding: ActivityCatalogueBinding
    private val model: CatalogueViewModel by lazy {
        ViewModelProvider(this, getVmFactory()).get(
            CatalogueViewModel::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_catalogue)
        binding.lifecycleOwner = this;
        binding.vm = model
        initAdapter()
        initSearch()

    }

    private fun initAdapter() {

        val adapter = ImagesAdapter() {
            model.retry()
        }
        binding.rvCatalogue.adapter = adapter

//
//        val gridLayoutManager = GridLayoutManager(this, COLUMNS, RecyclerView.VERTICAL, false)
//
//        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(position: Int): Int {
//                if (adapter.itemCount - 1 == position) {
//                    return COLUMNS
//                }
//                return 1
//            }
//        }

        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.rvCatalogue.layoutManager = linearLayoutManager;
        binding.rvCatalogue.addItemDecoration(SpacesItemDecoration(20))

        model.posts.observe(this, Observer {
            adapter.submitList(it) {
                val layoutManager = (binding.rvCatalogue.layoutManager as LinearLayoutManager)
                val position = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (position != RecyclerView.NO_POSITION) {
                    binding.rvCatalogue.scrollToPosition(position)
                }
            }
        })
        model.networkState.observe(this, Observer {
            adapter.setNetworkState(it)
        })
    }

    private fun initSearch() {

        binding.serchBtn.setOnClickListener {
            binding.serchBox.text?.trim()?.let {
                updateSearch(it.toString())
            }
        }
    }

    fun updateSearch(searchedText: String) {
        binding.rvCatalogue.scrollToPosition(0)
        (binding.rvCatalogue.adapter as? ImagesAdapter)?.submitList(null)
        model.fetchData(searchedText)
    }

    fun getVmFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CatalogueViewModel(
                    CatalogueRepo(
                        NetworkingProvider.apiService(FlickrApi::class.java),
                        this@CatalogueActivity
                    )
                ) as T
            }
        }
    }

}
