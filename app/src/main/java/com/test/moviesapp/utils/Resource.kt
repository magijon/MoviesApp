package com.test.moviesapp.utils

import com.test.moviesapp.utils.Resource.Status.*


data class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING,
        SUCCESSDB
    }

    companion object {
        fun <T> success(data: T): Resource<T>  = Resource(SUCCESS, data, null)

        fun <T> error(message: String, data: T? = null): Resource<T> = Resource(ERROR, data, message)

        fun <T> loading(data: T? = null): Resource<T> = Resource(LOADING, data, null)

        fun <T> successDB(data : T) : Resource<T> = Resource(SUCCESSDB, data, null)
    }
}