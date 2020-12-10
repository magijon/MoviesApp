package com.test.moviesapp.ui.fragments.entitiesUI

import com.test.moviesapp.data.entities.*
import java.util.concurrent.TimeUnit

data class MovieListUI(
    val name: String,
    val shortDescription: String,
    val shortName: String,
    val cover: String,
    val prLevel: PRLevel?,
    val externalId: String,
    val description: String,
    val imagePreview: String,
    val originalVersionTitle: String,
    val year: Int,
    val provider: String,
    val duration: String,
    val assetExternalId: String,
    var isFavorite: Boolean = false
) {
    companion object {

        private const val SHORTDESCRIPTION = "Pitch"
        private const val MAINCOVER = "GENERIC_COVER4_1"
        private const val MAINCOVER2 = "COVER4_1"
        private const val PREVIEW = "APP_SLSHOW_1"
        private const val PREVIEW2 = "APP_SLSHOW"
        private const val ORIGINALNAME = "originalVersionTitle"
        private const val SOMECOVER = "COVER"

        fun builder(movieGet: Movie?, errorGet: (() -> Unit)? = null): MovieListUI {
            var movieListUI: MovieListUI = getEmptyMovieListUI()
            movieGet?.let { movie ->
                movieListUI = MovieListUI(
                    name = movie.name ?: "",
                    shortDescription = movie.metadata?.let { metadata ->
                        metadata.find { it.name == SHORTDESCRIPTION }?.value ?: ""
                    } ?: "",
                    shortName = movie.shortName ?: "",
                    cover = movie.attachments?.let { images ->
                        images.find { it.name == MAINCOVER }?.value
                            ?: images.find { it.name == MAINCOVER2 }?.value
                            ?: images.find { it.name.contains(SOMECOVER) }?.value
                            ?: ""
                    } ?: "",
                    prLevel = PRLevel.values().find { it.prLevel == movie.prLevel },
                    externalId = movie.externalId ?: "",
                    description = movie.description ?: "",
                    imagePreview = movie.attachments?.let { images ->
                        images.find { it.name == PREVIEW }?.value
                            ?: images.find { it.name.contains(PREVIEW2) }?.value
                            ?: images[0].value
                    } ?: "",
                    originalVersionTitle = movie.metadata?.first { it.name == ORIGINALNAME }?.value
                        ?: "",
                    year = movie.year ?: 0,
                    provider = movie.contentProvider ?: "",
                    duration = String.format(
                        "%d minutos",
                        TimeUnit.MILLISECONDS.toMinutes(movie.duration ?: 0)
                    ),
                    assetExternalId = movie.assetExternalId ?: "",
                    isFavorite = movie.isFavorite
                )
            } ?: kotlin.run {
                errorGet?.invoke()
            }
            with(movieListUI) {
                if (name.isBlank() || cover.isBlank() || assetExternalId.isBlank())
                    errorGet?.invoke()
            }
            return movieListUI
        }

        private fun getEmptyMovieListUI(): MovieListUI =
            MovieListUI(
                "",
                "",
                "",
                "",
                PRLevel.ML_EMPTY,
                "",
                "",
                "",
                "",
                0,
                "",
                "",
                "",
            )


        fun builderList(movieList: List<Movie>): ArrayList<MovieListUI> =
            arrayListOf<MovieListUI>().apply {
                movieList.forEach { movie ->
                    this.add(builder(movie))
                }
            }

        fun builderListRecommended(recommendedList: List<Recommended>): java.util.ArrayList<MovieListUI> =
            arrayListOf<MovieListUI>().apply {
                recommendedList.forEach { recommended ->
                    this.add(
                        MovieListUI(
                            recommended.name,
                            "",
                            "",
                            recommended.images.find { it.name == MAINCOVER }?.value
                                ?: recommended.images.find { it.name == MAINCOVER2 }?.value
                                ?: recommended.images.find {
                                    it.name.contains(SOMECOVER) && !
                                    it.name.contains("COVER6_1")
                                }?.value
                                ?: "",
                            PRLevel.values().find { it.prLevel == recommended.prLevel },
                            recommended.externalContentId,
                            "",
                            "",
                            "",
                            -1,
                            "",
                            "",
                            ""
                        )
                    )
                }
            }

        fun builderFavoriteList(list: List<Favorite>): java.util.ArrayList<MovieListUI> =
            builderList(list.let {
                val movies: MutableList<Movie> = mutableListOf()
                it.map {
                    movies.add(
                        Movie(
                            listOf(
                                MetadataMovie("", SHORTDESCRIPTION, it.shortDescription),
                                MetadataMovie("", ORIGINALNAME, it.name)
                            ),
                            it.externalId, "",
                            it.name,
                            null,
                            null,
                            null,
                            null,
                            null,
                            it.prLevel,
                            listOf(Cover(name = MAINCOVER, value = it.cover)),
                            null,
                            null,
                            null,
                            it.assetExternalId,
                            true
                        )
                    )
                }
                movies
            })


    }
}