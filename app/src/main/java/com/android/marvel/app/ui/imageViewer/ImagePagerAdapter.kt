package com.android.marvel.app.ui.imageViewer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.marvel.R
import com.android.marvel.databinding.ItemComicPageBinding
import com.android.marvel.app.model.DetailItem
import com.android.marvel.app.model.getFullImageUrl

class ImageViewerPagerAdapter(private val items: List<DetailItem>) :
    RecyclerView.Adapter<ImageViewerPagerAdapter.ImageViewerPagerViewHolder>() {

    class ImageViewerPagerViewHolder(private val binding: ItemComicPageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item : DetailItem){
            binding.comicPageImage.load(item.thumbnail?.getFullImageUrl())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewerPagerViewHolder {
        val binding = ItemComicPageBinding.bind(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comic_page, parent, false))
        return ImageViewerPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewerPagerViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount() = items.size
}