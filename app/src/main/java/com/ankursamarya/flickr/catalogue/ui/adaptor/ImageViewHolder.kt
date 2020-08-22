package com.ankursamarya.flickr.catalogue.ui.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ankursamarya.flickr.R
import com.ankursamarya.flickr.catalogue.data.Image
import com.bumptech.glide.Glide

class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private var imageView: ImageView
    private var title: TextView

    init {
        imageView = itemView.findViewById(R.id.image) as ImageView
        title = itemView.findViewById(R.id.title) as TextView

    }

    fun bind(image: Image?) {
        Glide.with(itemView).load(image?.url).into(imageView)
        title.text = image?.title ?: ""
    }

    companion object {
        fun create(parent: ViewGroup): ImageViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.image_item, parent, false)
            return ImageViewHolder(
                view
            )
        }
    }
}