package com.ankursamarya.flickr.catalogue.ui.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ankursamarya.flickr.R
import com.ankursamarya.flickr.network.NetworkState
import com.ankursamarya.flickr.network.Status

class NetworkItemViewHolder(itemView: View, private val retryCallback: () -> Unit) :
    RecyclerView.ViewHolder(itemView) {

    val errorText: TextView
    val progress: ProgressBar
    val retryBtn: Button

    init {
        errorText = itemView.findViewById(R.id.error_msg)
        progress = itemView.findViewById(R.id.progress_bar)
        retryBtn = itemView.findViewById(R.id.retry_button)

        retryBtn.setOnClickListener {
            retryCallback()
        }
    }

    fun bind(nwState: NetworkState?) {
        itemView.visibility =
            if (nwState == null || nwState.status == Status.SUCCESS) View.GONE else View.VISIBLE

        retryBtn.visibility = if (nwState?.status == Status.FAILED) View.VISIBLE else View.GONE
        errorText.visibility = if (nwState?.status == Status.FAILED) View.VISIBLE else View.GONE
        errorText.text = nwState?.msg

        progress.visibility = if (nwState?.status == Status.RUNNING) View.VISIBLE else View.GONE
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit): NetworkItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.network_state_item, parent, false)
            return NetworkItemViewHolder(
                view, retryCallback
            );
        }
    }

}