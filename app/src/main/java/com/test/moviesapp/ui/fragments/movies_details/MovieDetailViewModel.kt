package com.test.moviesapp.ui.fragments.movies_details

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.test.moviesapp.data.entities.Movie
import com.test.moviesapp.data.entities.MovieResponse
import com.test.moviesapp.data.entities.RecommendedResponse
import com.test.moviesapp.data.repository.MovieRepository
import com.test.moviesapp.utils.Resource

class MovieDetailViewModel @ViewModelInject constructor(
    private val repository: MovieRepository
) : ViewModel() {

    var recommended: LiveData<Resource<RecommendedResponse>> = MutableLiveData()
    var movie: LiveData<Resource<MovieResponse>> = MutableLiveData()

    fun getMovie(id: String) {
        movie = repository.getMovie(id)
    }

    fun getRecommended(movie: Movie) {
        repository.getMoviesRecommended(movie).observeForever {
            (recommended as MutableLiveData).value = it
        }
    }

}