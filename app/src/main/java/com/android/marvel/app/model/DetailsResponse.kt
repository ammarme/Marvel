package com.android.marvel.app.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class DetailsResponse(
    val data: DetailsData
)

data class DetailsData(
    val results: List<DetailItem>
)

@Parcelize
data class DetailItem(
    val id: String = "",
    val title: String = "",
    val thumbnail: Thumbnail? = null
) : Parcelable