package com.android.marvel


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.marvel.databinding.ItemDetailsBinding

class ItemDetailsAdapter(
    private val items: List<Character>,
    private val onItemClick: (Character) -> Unit
) : RecyclerView.Adapter<ItemDetailsAdapter.ItemViewHolder>() {

    // ViewHolder Class
    class ItemViewHolder(private val binding: ItemDetailsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(character: Character, onItemClick: (Character) -> Unit) {
            binding.textViewName.text = character.name

            binding.imageViewHero.load(character.imageUrl) {
                placeholder(R.drawable.ic_launcher_foreground)
                error(R.drawable.ic_launcher_background)
            }

            // Handle click events
            binding.root.setOnClickListener {
                onItemClick(character)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemDetailsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position], onItemClick)
    }

    override fun getItemCount(): Int = items.size
}
