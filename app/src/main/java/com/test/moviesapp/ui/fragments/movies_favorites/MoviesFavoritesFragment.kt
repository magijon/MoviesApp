package com.test.moviesapp.ui.fragments.movies_favorites

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.test.moviesapp.R
import com.test.moviesapp.data.entities.Favorite
import com.test.moviesapp.databinding.MoviesFavoritesFragmentBinding
import com.test.moviesapp.ui.activities.MainActivity
import com.test.moviesapp.ui.activities.MainActivityViewModel
import com.test.moviesapp.ui.fragments.FragmentBase
import com.test.moviesapp.ui.fragments.entitiesUI.MovieListUI
import com.test.moviesapp.ui.fragments.movies.MoviesListAdapter
import com.test.moviesapp.utils.autoCleared
import com.test.moviesapp.utils.wrapperRepositoryResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFavoritesFragment : Fragment(), FragmentBase {

    private var binding: MoviesFavoritesFragmentBinding by autoCleared()
    private lateinit var mainActivity: MainActivity
    private lateinit var mainViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MoviesFavoritesFragmentBinding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity
        mainViewModel = mainActivity.viewModel
        mainActivity.title = getString(R.string.fragment_movies_favorites_title)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.loadFavorites()
        setupObservers()
        setupComponents()
    }

    private fun setupComponents() {
        binding.rvMoviesFavorites.adapter = getRecyclerFavorites()
    }

    private fun setupObservers() {
        mainViewModel.favorites.observe(viewLifecycleOwner) { resource ->
            wrapperRepositoryResponse(
                resource,
                mainActivity.progressBarStatus,
                {
                    loadRecyclerView(resource.data)
                },
                {
                    Toast.makeText(context, resource.message, Toast.LENGTH_LONG).show()
                    binding.tvNotFoundFavorites.visibility = View.VISIBLE
                },
                {
                    loadRecyclerView(resource.data)
                }
            )
        }
        mainActivity.searchText.observe(viewLifecycleOwner) {
            mainViewModel.search(it)
        }
    }

    private fun goToDetails(externalId: String) {
        if (mainActivity.orientation.value != Configuration.ORIENTATION_LANDSCAPE)
            goToDetails(
                bundleOf("externalId" to externalId),
                findNavController(),
                R.id.direction_from_moviesFavoritesFragment_to_movieDetailFragment,
            )
    }

    private fun getRecyclerFavorites(): MoviesListAdapter =
        MoviesListAdapter(MutableLiveData<String>().apply {
            observe(viewLifecycleOwner) {
                goToDetails(it)
            }
        }, MutableLiveData<Favorite>().apply {
            observe(viewLifecycleOwner) {
                mainViewModel.setFavorite(it)
            }
        })

    private fun loadRecyclerView(list: List<Favorite>?) {
        (binding.rvMoviesFavorites.adapter as MoviesListAdapter).let { adapter ->
            list?.let {
                adapter.setItems(MovieListUI.builderFavoriteList(it))
                binding.tvNotFoundFavorites.visibility =
                    if (it.isEmpty())
                        View.VISIBLE
                    else
                        View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        with(mainActivity.getSearchView()) {
            setQuery("", false)
            clearFocus()
        }
    }

}