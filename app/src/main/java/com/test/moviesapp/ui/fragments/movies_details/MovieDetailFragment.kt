package com.test.moviesapp.ui.fragments.movies_details

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.test.moviesapp.R
import com.test.moviesapp.data.entities.Favorite
import com.test.moviesapp.databinding.MovieDetailFragmentBinding
import com.test.moviesapp.ui.activities.MainActivity
import com.test.moviesapp.ui.activities.MainActivityViewModel
import com.test.moviesapp.ui.fragments.FragmentBase
import com.test.moviesapp.ui.fragments.entitiesUI.MovieListUI
import com.test.moviesapp.ui.fragments.movies.MoviesListAdapter
import com.test.moviesapp.utils.autoCleared
import com.test.moviesapp.utils.setColorFilter
import com.test.moviesapp.utils.wrapperRepositoryResponse
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieDetailFragment : Fragment(), FragmentBase {

    private lateinit var movieUI: MovieListUI
    private var isFavorite: Boolean = false
    private lateinit var menuItem: MenuItem
    private var binding: MovieDetailFragmentBinding by autoCleared()
    private val viewModel: MovieDetailViewModel by viewModels()
    private lateinit var mainActivity: MainActivity
    private lateinit var mainViewModel: MainActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MovieDetailFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        mainActivity = activity as MainActivity
        mainViewModel = mainActivity.viewModel
        mainActivity.title = getString(R.string.fragment_movie_detail_title)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString("externalId")?.let {
            viewModel.getMovie(it)
        }
        setupObservers()
    }

    private fun setupComponents(movie: MovieListUI) {
        with(binding) {
            mainActivity.title = if (movie.shortName.isNotBlank()) movie.shortName else movie.name
            yearText.text = resources.getString(R.string.year, movie.year.toString())
            providerText.text = movie.provider
            durationText.text = movie.duration
            prLevelText.text =
                movie.prLevel?.description ?: getString(R.string.pending_classification)
            movieDetailTitleOriginal.text = movie.originalVersionTitle
            movieDetailTitle.text = movie.name
            movieDetailDescription.text = movie.description
            Glide.with(root)
                .load("https://smarttv.orangetv.orange.es/stv/api/rtv/v1/images/${movie.cover}")
                .into(coverImageDetail)
            Glide.with(root)
                .load("https://smarttv.orangetv.orange.es/stv/api/rtv/v1/images/${movie.imagePreview}")
                .into(previewImage)
        }
        movieUI = MovieListUI.builder(viewModel.movie.value?.data?.response)
        mainViewModel.checkFavorite(movie.assetExternalId).observe(viewLifecycleOwner) {
            isFavorite = it?.let { true } ?: false
            setFavoriteIcon(isFavorite)
        }
    }

    private fun setFavoriteIcon(isFavorite: Boolean) {
        context?.let{
            val drawable = ContextCompat.getDrawable(
                it,
                if (isFavorite) R.drawable.ic_favorite_white else R.drawable.ic_not_favorite_white
            )
            menuItem.icon = drawable
        }
    }

    private fun updateFavorites(){
        with(movieUI) {
            mainViewModel.setFavorite(
                Favorite(
                    assetExternalId,
                    shortDescription,
                    externalId,
                    name,
                    prLevel?.prLevel ?: -1,
                    cover
                ))
        }
    }

    private fun setupObservers() {
        viewModel.movie.observe(viewLifecycleOwner) { resource ->
            wrapperRepositoryResponse(
                resource,
                mainActivity.progressBarStatus,
                {
                    resource.data?.let {
                        setupComponents(MovieListUI.builder(it.response) {
                            Toast.makeText(
                                context,
                                resources.getString(R.string.film_not_found),
                                Toast.LENGTH_LONG
                            ).show()
                            findNavController().popBackStack()
                        }
                        )
                        viewModel.getRecommended(it.response)
                    }
                },
                {
                    Toast.makeText(
                        context,
                        resource.message,
                        Toast.LENGTH_LONG
                    ).show()
                },
                null
            )
        }
        viewModel.recommended.observe(viewLifecycleOwner) { resource ->
            wrapperRepositoryResponse(
                resource,
                mainActivity.progressBarStatus,
                {
                    binding.rvRecommended.adapter =
                        MoviesListAdapter(MutableLiveData<String>().apply {
                            observe(viewLifecycleOwner) {
                                goToDetails(
                                    bundleOf("externalId" to it),
                                    findNavController(),
                                    R.id.direction_from_movieDetailFragment_to_movieDetailFragment
                                )
                            }
                        }, null, false)
                    resource.data?.let {
                        (binding.rvRecommended.adapter as MoviesListAdapter).setItems(
                            MovieListUI.builderListRecommended(
                                it.response
                            )
                        )
                    }
                    binding.scroll.scrollTo(0, 0)
                },
                {
                    Toast.makeText(context, resource.message, Toast.LENGTH_LONG).show()
                },
                null
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_navbar, menu)
        this.menuItem = menu.findItem(R.id.favoriteMenu)
        menuItem.isVisible = true
        menuItem.setOnMenuItemClickListener {
            isFavorite = !isFavorite
            setFavoriteIcon(isFavorite)
            updateFavorites()
            true
        }
    }

    override fun onPause() {
        super.onPause()
        menuItem.isVisible = false
    }

}