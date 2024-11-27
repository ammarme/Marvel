package com.android.marvel.app.ui.details

import com.android.marvel.app.model.Character

sealed class DetailsState {
    data object Idle : DetailsState()
    data object Loading : DetailsState()
    data class Success(val character: Character) : DetailsState()
    data class Error(val message: String) : DetailsState()
}