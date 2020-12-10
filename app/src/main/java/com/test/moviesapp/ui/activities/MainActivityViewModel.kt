package com.test.moviesapp.ui.activities

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.moviesapp.data.entities.Favorite
import com.test.moviesapp.data.repository.MovieRepository
import com.test.moviesapp.utils.Resource

class MainActivityViewModel @ViewModelInject constructor(
    val repository: MovieRepository
) : ViewModel() {

    var favoritesFull: List<Favorite>? = null
    var favorites: LiveData<Resource<List<Favorite>>> = MutableLiveData()

    fun loadFavorites() {
        repository.getFavorites(favorites)
    }

    fun setFavorite(favorite: Favorite) {
        repository.updateFavorite(
            favorite,
            viewModelScope
        ) {
            loadFavorites()
        }
    }

    fun checkFavorite(assetExternalId : String) : LiveData<Favorite?> = repository.checkFavorite(assetExternalId)

    fun search(text: String) {
        (favorites as MutableLiveData).let { movies ->
            movies.value?.data?.let {list ->
                favoritesFull?.let { listDB ->
                    movies.value = Resource.success(
                        listDB.filter { movie ->
                            movie.name.contains(text, true)
                        })
                } ?: kotlin.run {
                    favoritesFull = list
                    search(text)
                }
            }
        }
    }

}