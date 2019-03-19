package com.sg.findfood.model.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * created by sandeep gupta on 17/3/19
 */

@Entity
 data class FavouriteVenue(
@PrimaryKey(autoGenerate = true)
  var id: Long? = null,
  var server_venue_key: String) {
    constructor(name: String) : this(null,name)
}
