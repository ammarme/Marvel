package com.android.marvel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Character(
    val name: String,
    val imageUrl: String
) : Parcelable

// Dummy data
val dummyCharacters = listOf(
    Character("3-D Man", "https://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784.jpg"),
    Character("A-Bomb", "https://i.annihil.us/u/prod/marvel/i/mg/3/20/5232158de5b16.jpg"),
    Character("A.I.M.", "https://i.annihil.us/u/prod/marvel/i/mg/6/20/52602f21f29ec.jpg"),
    Character("Abomination", "https://i.annihil.us/u/prod/marvel/i/mg/9/50/4ce18691cbf04.jpg")
)

