package com.ankursamarya.flickr.catalogue.ui.adaptor

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ankursamarya.flickr.R
import com.ankursamarya.flickr.catalogue.data.Image
import com.ankursamarya.flickr.network.NetworkState

class ImagesAdapter(private val retryCallback: () -> Unit) :
    PagedListAdapter<Image, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Image>() {

            private val PAYLOAD_SCORE = Any()

            override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
                return oldItem.url == newItem.url
            }

            override fun getChangePayload(oldItem: Image, newItem: Image): Any? {
                return if (oldItem.url == newItem.url) {
                    PAYLOAD_SCORE
                } else {
                    null
                }
            }
        }
    }

    private var networkState: NetworkState? = null;

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.network_state_item -> NetworkItemViewHolder.create(
                parent, retryCallback
            )
            R.layout.image_item -> ImageViewHolder.create(
                parent
            )
            else -> throw RuntimeException("not supported")
        }
    }


    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val item = getItem(position)
            (holder as ImageViewHolder).bind(item)
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (getItemViewType(position)) {
            R.layout.image_item -> (holder as ImageViewHolder).bind(getItem(position))
            R.layout.network_state_item -> (holder as NetworkItemViewHolder).bind(networkState)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) {
            R.layout.network_state_item
        } else {
            R.layout.image_item
        }
    }

    fun setNetworkState(networkState: NetworkState) {
        val previousState = this.networkState

        this.networkState = networkState

        if (previousState != networkState) {
            notifyItemChanged(itemCount - 1)
        }


    }

}