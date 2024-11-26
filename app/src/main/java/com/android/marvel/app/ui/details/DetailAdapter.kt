package com.android.marvel.app.ui.details


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.marvel.databinding.ItemDetailsBinding
import com.android.marvel.app.model.DetailItem
import com.android.marvel.app.model.getFullImageUrl

class DetailsAdapter(private var items: MutableList<DetailItem>) :
    RecyclerView.Adapter<DetailsAdapter.ItemViewHolder>() {

    class ItemViewHolder(
        private val binding: ItemDetailsBinding,
        private val items: List<DetailItem>
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(detailItem: DetailItem) {
            binding.textViewName.text = detailItem.title
            binding.imageViewHero.load(detailItem.thumbnail?.getFullImageUrl())

            binding.root.setOnClickListener {
                val action = DetailsFragmentDirections
                    .actionCharacterDetailsFragmentToViewerFragment(
                        adapterPosition,
                        items.toTypedArray()
                    )
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
        return ItemViewHolder(binding, items)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun updateData(newItems: List<DetailItem>) {
        val diffResult = DiffUtil.calculateDiff(DetailsDiffCallback(items, newItems))
        items.clear()
        items.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }
}
