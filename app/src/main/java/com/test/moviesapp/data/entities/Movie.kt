package com.test.moviesapp.data.entities

data class Movie(
    val metadata : List<MetadataMovie>?,
    val externalId : String?,
    val id: String?,
    val name: String?,
    val description: String?,
    val shortName: String?,
    val keywords: String?,
    val year: Int?,
    val contentProvider: String?,
    val prLevel: Int?,
    val attachments: List<Cover>?,
    val duration: Long?,
    val genreEntityList: List<Genre>?,
    val definition: String?,
    val assetExternalId : String?,
    var isFavorite: Boolean = false
)


