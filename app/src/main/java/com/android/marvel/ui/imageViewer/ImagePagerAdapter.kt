package com.android.marvel.ui.imageViewer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.marvel.R
import com.android.marvel.databinding.ItemComicPageBinding
import com.android.marvel.ui.model.DetailItem

class ComicPagerAdapter(private val items: List<DetailItem>) :
    RecyclerView.Adapter<ComicPagerAdapter.ComicPageViewHolder>() {

    class ComicPageViewHolder(private val binding: ItemComicPageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item : DetailItem){
            binding.comicPageImage.load(item.imageUrl)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicPageViewHolder {
        val binding = ItemComicPageBinding.bind(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comic_page, parent, false))
        return ComicPageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComicPageViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount() = items.size
}