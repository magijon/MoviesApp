package com.test.moviesapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.test.moviesapp.data.entities.*
import com.test.moviesapp.data.local.FavoriteDao
import com.test.moviesapp.data.remote.MovieRemoteDataSource
import com.test.moviesapp.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val remoteDataSource: MovieRemoteDataSource,
    private val localDataSource: FavoriteDao
) {


    fun getMoviesList(): LiveData<Resource<MovieListResponse>> =
        getDataFromApi { remoteDataSource.getMoviesList() }

    fun getMovie(id: String): LiveData<Resource<MovieResponse>> =
        getDataFromApi { remoteDataSource.getMovie(id) }

    fun getMoviesRecommended(movie: Movie): LiveData<Resource<RecommendedResponse>> =
        getDataFromApi { remoteDataSource.getMovieRecommended(movie) }

    fun getFavorites(favoritesLiveData: LiveData<Resource<List<Favorite>>>) {
        runBlocking(Dispatchers.IO) {
            with (favoritesLiveData as MutableLiveData) {
                postValue(Resource.loading(null))
                val favorites = localDataSource.getAllFavorites()
                favorites?.let {
                    postValue(Resource.success(it))
                    postValue(Resource.successDB(it))
                } ?: kotlin.run {
                    postValue(Resource.error("No hay favoritos", null))
                }
            }
        }
    }


    private fun addFavorite(favorite: Favorite, viewModelScope: CoroutineScope) {
        viewModelScope.launch {
            localDataSource.insert(favorite)
        }
    }

    private fun deleteFavorite(assetExternalId: String) {
        runBlocking(Dispatchers.IO) {
            localDataSource.deleteByExternalId(assetExternalId)
        }
    }

    fun checkFavorite(
        assetExternalId: String
    ): LiveData<Favorite?> =
        liveData(Dispatchers.IO) {
            val response = localDataSource.getFavorite(assetExternalId)
            response?.let {
                emit(it)
            } ?: kotlin.run {
                emit(null)
            }
        }


    fun updateFavorite(
        favorite: Favorite,
        viewModelScope: CoroutineScope,
        onUpdate: (() -> Unit)? = null
    ) {
        checkFavorite(favorite.assetExternalId).observeForever {
            it?.let {
                deleteFavorite(it.assetExternalId)
            } ?: kotlin.run {
                addFavorite(favorite, viewModelScope)
            }
            onUpdate?.invoke()
        }
    }

}