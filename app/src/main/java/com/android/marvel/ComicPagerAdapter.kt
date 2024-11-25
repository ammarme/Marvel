package com.android.marvel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.marvel.databinding.ItemComicPageBinding

class ComicPagerAdapter(private val characters: List<Character>) :
    RecyclerView.Adapter<ComicPagerAdapter.ComicPageViewHolder>() {

    class ComicPageViewHolder(private val binding: ItemComicPageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(character : Character){
            binding.comicPageImage.load(character.imageUrl)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComicPageViewHolder {
        val binding = ItemComicPageBinding.bind(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comic_page, parent, false))
        return ComicPageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ComicPageViewHolder, position: Int) {
        holder.onBind(characters[position])
    }

    override fun getItemCount() = characters.size
}