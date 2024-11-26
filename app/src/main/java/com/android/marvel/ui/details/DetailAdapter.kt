package com.android.marvel.ui.details


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.marvel.databinding.ItemDetailsBinding
import com.android.marvel.ui.model.DetailItem

class DetailsAdapter(
    private val items: MutableList<DetailItem>,
    private val onItemClick: (DetailItem) -> Unit
) : RecyclerView.Adapter<DetailsAdapter.ItemViewHolder>() {

    // ViewHolder Class
    class ItemViewHolder(private val binding: ItemDetailsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(detailItem: DetailItem, onItemClick: (DetailItem) -> Unit) {
            binding.textViewName.text = detailItem.name

            binding.imageViewHero.load(detailItem.imageUrl)

            // Handle click events
            binding.root.setOnClickListener {
                onItemClick(detailItem)
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

    fun insertData(detailItemList: List<DetailItem>) {
        this.items.clear()
        this.items.addAll(detailItemList)
        notifyDataSetChanged()
    }
}
