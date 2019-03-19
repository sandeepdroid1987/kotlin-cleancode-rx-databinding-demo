package com.sg.findfood.model.local.room

import androidx.room.*

/**
 * created by sandeep gupta on 17/3/19
 */

@Dao
interface FavouriteVenueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavourite(venue: FavouriteVenue)


    @Query("SELECT count(*) FROM FavouriteVenue WHERE server_venue_key == :server_venue_key")
    fun getFavourite(server_venue_key: String): Int


    @Query("DELETE FROM FavouriteVenue WHERE server_venue_key = :server_venue_key")
    fun deleteFavouriteVenue(server_venue_key: String): Int
}