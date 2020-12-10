package com.test.moviesapp.data.remote

import com.test.moviesapp.data.entities.Movie
import javax.inject.Inject

class MovieRemoteDataSource @Inject constructor(
    private val moviesService: MoviesService
) : BaseDataSource() {

    suspend fun getMoviesList() = getResult {
        moviesService.getMovieList()
    }

    suspend fun getMovie(id: String) = getResult {
        moviesService.getMovie(externalId = id)
    }

    suspend fun getMovieRecommended(movie: Movie) = getResult {
        moviesService.getRecommended(
            params = "external_content_id:${movie.assetExternalId}",
            maxPRLevel = movie.prLevel ?: 0,
            quality = movie.definition ?: ""
        )
    }

}