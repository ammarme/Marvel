package com.android.marvel.app.ui.home

import com.android.marvel.app.model.Character

sealed class HomeState {
    data object Idle : HomeState()
    data object Loading : HomeState()
    data class Success(val characters: List<Character>) : HomeState()
    data class Error(val message: String) : HomeState()
}