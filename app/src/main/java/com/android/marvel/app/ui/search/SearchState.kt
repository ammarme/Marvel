package com.android.marvel.app.ui.search

import com.android.marvel.app.model.Character

sealed class SearchState {
    data object Idle : SearchState()
    data object Loading : SearchState()
    data class Success(val characters: List<Character>) : SearchState()
    data class Error(val message: String) : SearchState()
}