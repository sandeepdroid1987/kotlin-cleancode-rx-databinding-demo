package com.sg.findfood.model.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * created by sandeep gupta on 17/3/19
 */

@Parcelize
data class SearchResponse(
    @SerializedName("response") val data: SearchDataResponse?
) : Parcelable

@Parcelize
data class SearchDataResponse(
    val venues: List<Venue>?
) : Parcelable

@Parcelize
data class Venue(
    val id: String,
    val name: String?,
    val location: Location?,
    val categories: List<Category>?,
    var favourite : Boolean? = false
) : Parcelable

@Parcelize
data class Location(
    val address: String?,
    val lat: Double?,
    val lng: Double?
) : Parcelable

@Parcelize
data class Category(
    val name: String,
    val primary: Boolean?,
    val icon: Icon?,
    val url: String? =  "www.google.com"
) : Parcelable

@Parcelize
data class Icon(
    val prefix: String?,
    val suffix: String?
) : Parcelable
