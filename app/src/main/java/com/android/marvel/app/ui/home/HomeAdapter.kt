package com.android.marvel.app.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.marvel.R
import com.android.marvel.app.model.Character
import com.android.marvel.app.model.getFullImageUrl
import com.android.marvel.databinding.ItemMarvelCharacterBinding

class HomeAdapter(
    private val characterList: MutableList<Character>
) : RecyclerView.Adapter<HomeAdapter.CharacterViewHolder>() {

    inner class CharacterViewHolder(private val binding: ItemMarvelCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(character: Character) {
            binding.itemTextView.text = character.name
            val imageUrl = character.thumbnail?.getFullImageUrl()

            binding.itemImageView.load(imageUrl) {
                placeholder(R.color.black)
                error(R.color.black)
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

    fun updateCharacters(newList: List<Character>) {
        val diffCallback = HomeDiffCallback(characterList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        characterList.clear()
        characterList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }
}