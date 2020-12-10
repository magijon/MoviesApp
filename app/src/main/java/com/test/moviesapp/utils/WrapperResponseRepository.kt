package com.test.moviesapp.utils

import androidx.lifecycle.MutableLiveData
import com.test.moviesapp.ui.activities.Status


fun <T> wrapperRepositoryResponse(
    resource: Resource<T>,
    progressBar: MutableLiveData<Status>,
    onSuccess: (() -> Unit)?,
    onError: (() -> Unit)?,
    onSuccessDB: (() -> Unit)?
) {

    when (resource.status) {
        Resource.Status.SUCCESS -> {
            progressBar.value = Status.GONE
            onSuccess?.invoke()
        }
        Resource.Status.ERROR -> {
            progressBar.value = Status.GONE
            onError?.invoke()
        }
        Resource.Status.LOADING -> {
            progressBar.value = Status.VISIBLE
        }
        Resource.Status.SUCCESSDB -> {
            progressBar.value = Status.GONE
            onSuccessDB?.invoke()
        }
    }
}