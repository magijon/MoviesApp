package com.test.moviesapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.test.moviesapp.utils.Resource
import kotlinx.coroutines.Dispatchers

fun <T> getDataFromApi(call : suspend () -> Resource<T>): LiveData<Resource<T>> =
    liveData(Dispatchers.IO) {
        emit(Resource.loading())
        val responseStatus: Resource<T> = call.invoke()
        responseStatus.data?.let {
            when (responseStatus.status) {
                Resource.Status.SUCCESS -> {
                    emit(Resource.success(it))
                }
                Resource.Status.ERROR -> {
                    responseStatus.message?.let {message ->
                        emit(Resource.error<T>(message))
                    }
                }
                else -> {
                }
            }
        } ?: kotlin.run {
            responseStatus.message?.let {message ->
                emit(Resource.error<T>(message))
            }
        }
    }