package com.test.moviesapp.ui.activities

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.tabs.TabLayout
import com.test.moviesapp.R
import com.test.moviesapp.databinding.ActivityMainBinding
import com.test.moviesapp.ui.fragments.movies.MoviesFragment
import com.test.moviesapp.ui.fragments.movies_favorites.MoviesFavoritesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    var orientation: MutableLiveData<Int> = MutableLiveData()
    private lateinit var binding: ActivityMainBinding
    var progressBarStatus: MutableLiveData<Status> = MutableLiveData()
    var searchText : MutableLiveData<String> = MutableLiveData()

    private lateinit var fragmentLeft : MoviesFragment
    private lateinit var fragmentRight : MoviesFavoritesFragment

    val viewModel: MainActivityViewModel by viewModels()

    private var currentFragmentId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.moviesFragment, R.id.moviesFavoritesFragment))
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        configObservers(navController)
        configNavigation(navController)
        configureSearchView()
    }

    private fun configObservers(navController: NavController) {
        with(binding) {
            progressBarStatus.observe(this@MainActivity) {
                binding.progressBar.visibility = it.value
            }

            tabLayout.addOnTabSelectedListener(object :
                TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
                override fun onTabSelected(tab: TabLayout.Tab) {
                    navController.navigate(
                        when (tab.position) {
                            0 -> {
                                R.id.direction_from_moviesFavoritesFragment_to_moviesFragment
                            }
                            1 -> {
                                R.id.direction_from_moviesFragment_to_moviesFavoritesFragment
                            }
                            else -> {
                                0
                            }
                        }
                    )
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    //TODO("Not yet implemented")
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    //TODO("Not yet implemented")
                }
            })
            orientation.observe(this@MainActivity) {
                when (it) {
                    Configuration.ORIENTATION_LANDSCAPE -> {
                        fragmentLeft = MoviesFragment()
                        fragmentRight = MoviesFavoritesFragment()
                        supportFragmentManager.beginTransaction()
                            .add(R.id.fragmentContainerLeft, fragmentLeft)
                            .add(R.id.fragmentContainerRight, fragmentRight)
                            .commit()
                        tabLayout.visibility = GONE
                        navHostFragment.visibility = GONE
                        fragmentContainerLeft.visibility = VISIBLE
                        fragmentContainerRight.visibility = VISIBLE
                    }
                    Configuration.ORIENTATION_PORTRAIT -> {
                        supportFragmentManager.beginTransaction()
                            .remove(fragmentLeft)
                            .remove(fragmentRight)
                            .commit()
                        tabLayout.visibility = VISIBLE
                        navHostFragment.visibility = VISIBLE
                        fragmentContainerLeft.visibility = GONE
                        fragmentContainerRight.visibility = GONE
                    }
                }
            }
        }
    }


    private fun configNavigation(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentFragmentId = destination.id
            with(binding) {
                when (currentFragmentId) {
                    R.id.moviesFavoritesFragment -> {
                        tabLayout.visibility = VISIBLE
                        searchView.visibility = VISIBLE
                    }
                    R.id.moviesFragment -> {
                        tabLayout.visibility = VISIBLE
                        searchView.visibility = VISIBLE
                    }
                    else -> {
                        tabLayout.visibility = GONE
                        searchView.visibility = GONE
                    }
                }
            }
        }
    }


    override fun onBackPressed() {
        if (currentFragmentId != R.id.moviesFragment && currentFragmentId != R.id.moviesFavoritesFragment)
            super.onBackPressed()
    }

    fun getSearchView() = binding.searchView

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        orientation.value = newConfig.orientation
    }

    fun configureSearchView() {
        with(binding.searchView) {
            setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        query?.let {
                            searchText.postValue(it)
                            return true
                        } ?: kotlin.run {
                            return false
                        }
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        newText?.let {
                            searchText.postValue(it)
                            return true
                        } ?: kotlin.run {
                            return false
                        }
                    }
                })
        }
    }

}

enum class Status(val value: Int) {
    VISIBLE(View.VISIBLE),
    GONE(View.GONE)
}