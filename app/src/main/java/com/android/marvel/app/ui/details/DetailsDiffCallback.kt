package com.android.marvel.app.ui.details

import androidx.recyclerview.widget.DiffUtil
import com.android.marvel.app.model.DetailItem

class DetailsDiffCallback(
    private val oldList: List<DetailItem>,
    private val newList: List<DetailItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
