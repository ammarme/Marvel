package com.android.marvel.app.ui.home

import androidx.recyclerview.widget.DiffUtil
import com.android.marvel.app.model.Character

class HomeDiffCallback(
    private val oldList: List<Character>,
    private val newList: List<Character>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Check if items represent the same character
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Check if content has changed
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
