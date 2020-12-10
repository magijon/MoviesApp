package com.test.moviesapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class Favorite (
    @PrimaryKey
    val assetExternalId : String,
    val shortDescription : String,
    val externalId : String,
    val name: String,
    val prLevel: Int,
    val cover: String
)