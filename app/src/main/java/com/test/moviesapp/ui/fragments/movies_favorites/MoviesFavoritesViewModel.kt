package com.test.moviesapp.ui.fragments.movies_favorites
/*
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.moviesapp.data.entities.Favorite
import com.test.moviesapp.data.repository.MovieRepository
import com.test.moviesapp.utils.Resource

class MoviesFavoritesViewModel @ViewModelInject constructor(
    val repository: MovieRepository
) : ViewModel() {

    var favoritesFull: List<Favorite>? = null
    var favorites: LiveData<Resource<List<Favorite>>> = repository.getFavorites()

    fun setFavorite(favorite: Favorite) {
        repository.updateFavorite(
            favorite,
            viewModelScope
        ) {
            repository.getFavorites().observeForever {
                if (it.status == Resource.Status.SUCCESSDB)
                    (favorites as MutableLiveData).value = it
            }
        }
    }

    fun search(text: String) {
        (favorites as MutableLiveData).let { movies ->
            movies.value?.data?.let {
                favoritesFull?.let { listDB ->
                    movies.value = Resource.successDB(
                        listDB.filter { movie ->
                            movie.name.contains(text, true)
                        })
                }
            }
        }
    }

}*/