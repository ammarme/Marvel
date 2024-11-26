package com.android.marvel.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.marvel.R
import com.android.marvel.databinding.ItemMarvelCharacterBinding
import com.android.marvel.ui.model.Character

class HomeAdapter(private val characterList: MutableList<Character>) :
    RecyclerView.Adapter<HomeAdapter.CharacterViewHolder>() {

    // ViewHolder for each item
    class CharacterViewHolder(private val binding: ItemMarvelCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character) {
            binding.itemTextView.text = character.name
            val imageUrl = "${character.thumbnail?.path}.${character.thumbnail?.extension}"

            binding.itemImageView.load(imageUrl) {
                placeholder(R.color.colorDividerLight)
                error(R.color.colorPrimaryRed)
            }

            binding.root.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionMarvelCharactersFragmentToCharacterDetailsFragment(
                        character
                    )
                Navigation.findNavController(binding.root).navigate(action)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ItemMarvelCharacterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characterList[position])
    }

    override fun getItemCount(): Int = characterList.size

    fun addCharacters(listCharacters: List<Character>) {
        characterList.clear()
        characterList.addAll(listCharacters)
        notifyDataSetChanged()
    }
}
