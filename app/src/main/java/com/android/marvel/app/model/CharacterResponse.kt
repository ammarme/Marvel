package com.android.marvel.app.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class CharactersResponse(
    val data: CharacterData
)

data class CharacterData(
    val results: List<Character>
)

@Parcelize
data class Character(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val thumbnail: Thumbnail? = null,
    val urls: List<Url>? = null
) : Parcelable

@Parcelize
data class Thumbnail(
    val path: String = "",
    val extension: String = ""
) : Parcelable

@Parcelize
data class Url(
    val type: String,
    val url: String
) : Parcelable

fun Thumbnail.getFullImageUrl(): String {
    return "${path}.${extension}"
}