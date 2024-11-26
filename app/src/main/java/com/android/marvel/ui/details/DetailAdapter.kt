package com.android.marvel.ui.details


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.marvel.databinding.ItemDetailsBinding
import com.android.marvel.ui.model.DetailItem

class DetailsAdapter(
    private val items: MutableList<DetailItem>
) : RecyclerView.Adapter<DetailsAdapter.ItemViewHolder>() {

    // ViewHolder Class
    class ItemViewHolder(private val binding: ItemDetailsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int, items: MutableList<DetailItem>) {
            binding.textViewName.text = items[position].name

            binding.imageViewHero.load(items[position].imageUrl)

            // Handle click events
            binding.root.setOnClickListener {
                val action = CharacterDetailsFragmentDirections.actionCharacterDetailsFragmentToViewerFragment(position, items.toTypedArray())
                Navigation.findNavController(binding.root).navigate(action)
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
        holder.bind(position , items)
    }

    override fun getItemCount(): Int = items.size

    fun insertData(detailItemList: List<DetailItem>) {
        this.items.clear()
        this.items.addAll(detailItemList)
        notifyDataSetChanged()
    }
}
