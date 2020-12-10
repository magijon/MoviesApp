package com.test.moviesapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.test.moviesapp.data.entities.Favorite

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite")
    fun getAllFavorites() : List<Favorite>?

    @Query("SELECT * FROM favorite WHERE assetExternalId = :assetExternalId")
    fun getFavorite(assetExternalId : String) : Favorite?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: Favorite)

    @Query("DELETE FROM favorite WHERE assetExternalId = :assetExternalId")
    fun deleteByExternalId(assetExternalId: String)
}

