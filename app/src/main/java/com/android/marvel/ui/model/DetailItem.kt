package com.android.marvel.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailItem (val name : String, var imageUrl : String) : Parcelable