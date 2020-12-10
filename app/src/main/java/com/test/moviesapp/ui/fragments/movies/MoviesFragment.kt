package com.test.moviesapp.ui.fragments.movies

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.test.moviesapp.R
import com.test.moviesapp.data.entities.Favorite
import com.test.moviesapp.databinding.MoviesFragmentBinding
import com.test.moviesapp.ui.activities.MainActivity
import com.test.moviesapp.ui.activities.MainActivityViewModel
import com.test.moviesapp.ui.fragments.FragmentBase
import com.test.moviesapp.ui.fragments.entitiesUI.MovieListUI
import com.test.moviesapp.utils.autoCleared
import com.test.moviesapp.utils.wrapperRepositoryResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment(), FragmentBase {

    private var binding: MoviesFragmentBinding by autoCleared()
    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var mainActivity: MainActivity
    private lateinit var mainViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MoviesFragmentBinding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity
        mainViewModel = mainActivity.viewModel
        mainActivity.title = getString(R.string.fragment_movies_title)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMovies()
        mainViewModel.loadFavorites()
        setupComponents()
        setupObservers()
    }

    private fun setupComponents() {
        binding.rvMovies.adapter = getMoviesListAdapter()
    }

    private fun setupObservers() {
        viewModel.movies.observe(viewLifecycleOwner) { resource ->
            wrapperRepositoryResponse(
                resource,
                mainActivity.progressBarStatus,
                {
                    resource.data?.let { viewModel.moviesFull = it }
                    loadAdapterRecylcer()
                },
                { Toast.makeText(context, resource.message, Toast.LENGTH_LONG).show() },
                {loadAdapterRecylcer()}
            )
        }
        mainViewModel.favorites.observe(viewLifecycleOwner) { resource ->
            wrapperRepositoryResponse(
                resource,
                mainActivity.progressBarStatus,
                null,
                { Toast.makeText(context, resource.message, Toast.LENGTH_LONG).show() },
                { loadAdapterRecylcer() }
            )
        }
        mainActivity.searchText.observe(viewLifecycleOwner){
            viewModel.search(it)
        }
    }

    private fun loadAdapterRecylcer() {
        viewModel.movies.value?.data?.response?.let {
            mainViewModel.favorites.value?.data?.let { favorites ->
                viewModel.updateMoviesWithFavorites(favorites)?.let { moviesUpdate ->
                    (binding.rvMovies.adapter as MoviesListAdapter).setItems(
                        MovieListUI.builderList(moviesUpdate)
                    )
                }
            }
        }
    }

    private fun getMoviesListAdapter(): MoviesListAdapter =
        MoviesListAdapter(MutableLiveData<String>().apply {
            observe(viewLifecycleOwner) {
                if (mainActivity.orientation.value != Configuration.ORIENTATION_LANDSCAPE)
                    goToDetails(
                        bundleOf("externalId" to it),
                        findNavController(),
                        R.id.direction_from_moviesFragment_to_movieDetailFragment
                    )
            }
        }, MutableLiveData<Favorite>().apply {
            observe(viewLifecycleOwner) {
                mainViewModel.setFavorite(it)
            }
        })

    override fun onResume() {
        super.onResume()
        with(mainActivity.getSearchView()) {
            setQuery("", false)
            clearFocus()
        }
    }

}