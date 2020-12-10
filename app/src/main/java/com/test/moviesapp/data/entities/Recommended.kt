package com.test.moviesapp.data.entities

data class Recommended (
    val name : String,
    val prLevel : Int,
    val externalContentId : String,
    val images: List<Cover>
)
