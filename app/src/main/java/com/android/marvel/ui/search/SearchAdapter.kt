package com.android.marvel.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.marvel.R
import com.android.marvel.databinding.ListItemSearchBinding
import com.android.marvel.ui.model.Character

class SearchAdapter(private val itemList: MutableList<Character>) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

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
            val imageUrl = "${character.thumbnail?.path}.${character.thumbnail?.extension}"

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

    fun setList(newList: List<Character>){
        itemList.clear()
        itemList.addAll(newList)
        notifyDataSetChanged()
    }
}
