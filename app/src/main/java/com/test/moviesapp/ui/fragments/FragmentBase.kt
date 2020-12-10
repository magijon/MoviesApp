package com.test.moviesapp.ui.fragments

import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController

interface FragmentBase {
    fun goToDetails(
        bundle: Bundle,
        navController: NavController,
        directions: Int
    ) {
        navController.navigate(
            directions,
            bundle
        )
    }
}