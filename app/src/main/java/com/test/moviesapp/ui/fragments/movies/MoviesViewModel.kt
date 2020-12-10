package com.test.moviesapp.ui.fragments.movies

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.test.moviesapp.data.entities.Favorite
import com.test.moviesapp.data.entities.Movie
import com.test.moviesapp.data.entities.MovieListResponse
import com.test.moviesapp.data.repository.MovieRepository
import com.test.moviesapp.utils.Resource


class MoviesViewModel @ViewModelInject constructor(
    val repository: MovieRepository
) : ViewModel() {

    lateinit var moviesFull: MovieListResponse
    var movies: LiveData<Resource<MovieListResponse>> = MutableLiveData()


    fun getMovies() {
        movies = repository.getMoviesList()
    }

    fun search(text: String) {
        (movies as MutableLiveData).let { movies ->
            movies.value?.data?.let {
                movies.value = Resource.successDB(MovieListResponse(
                    it.metadata,
                    moviesFull.response.filter { movie ->
                        movie.name?.contains(text, true) ?: false
                    }
                ))
            }
        }
    }

    fun updateMoviesWithFavorites(favorites: List<Favorite>) : List<Movie>? {
        val newListMovies = movies.value?.data?.response
        newListMovies?.map {movie ->
            movie.isFavorite = favorites.map { it.assetExternalId == movie.assetExternalId }.contains(true)
        }
        return newListMovies
    }

}