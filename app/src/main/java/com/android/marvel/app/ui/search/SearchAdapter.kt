package com.android.marvel.app.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.marvel.R
import com.android.marvel.databinding.ListItemSearchBinding
import com.android.marvel.app.ui.home.HomeDiffCallback
import com.android.marvel.app.model.Character
import com.android.marvel.app.model.getFullImageUrl

class SearchAdapter(private var itemList: MutableList<Character>) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ListItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int = itemList.size

    inner class SearchViewHolder(private val binding: ListItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character) {
            binding.textView.text = character.name
            val imageUrl = character.thumbnail?.getFullImageUrl()

            binding.imageView.load(imageUrl) {
                placeholder(R.color.colorDividerLight)
                error(R.color.colorPrimaryRed)
            }

            binding.root.setOnClickListener {
                val action = SearchFragmentDirections.actionSearchFragmentToCharacterDetailsFragment(character)
                Navigation.findNavController(binding.root).navigate(action)
            }
        }
    }

    fun setList(newList: List<Character>) {
        val diffCallback = HomeDiffCallback(itemList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        itemList.clear()
        itemList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
}
