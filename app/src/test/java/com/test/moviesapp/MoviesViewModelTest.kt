package com.test.moviesapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.test.moviesapp.data.entities.Movie
import com.test.moviesapp.data.entities.MovieListResponse
import com.test.moviesapp.data.repository.MovieRepository
import com.test.moviesapp.ui.activities.MainActivityViewModel
import com.test.moviesapp.ui.fragments.movies.MoviesViewModel
import com.test.moviesapp.utils.Resource
import kotlinx.android.synthetic.main.activity_main.view.*
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations


@RunWith(JUnit4::class)
class MoviesViewModelTest : BaseTest() {

    @Mock
    lateinit var mockMoviesRepository: MovieRepository

    @Mock
    private lateinit var mockLiveDataResourceMovies: LiveData<Resource<MovieListResponse>>

    @Mock
    private lateinit var mockMovie1: Movie

    @Mock
    private lateinit var mockMovie2: Movie




    lateinit var viewModel: MoviesViewModel

    override fun setup() {
        MockitoAnnotations.initMocks(this)
        this.viewModel = MoviesViewModel(this.mockMoviesRepository)

        `when`(mockMovie1.assetExternalId).thenReturn("assetExternalId")
        `when`(mockMovie1.description).thenReturn("description")
        `when`(mockMovie1.name).thenReturn("name")
        `when`(mockMovie1.prLevel).thenReturn(1)
        `when`(mockMovie1.duration).thenReturn(100)
        `when`(mockMovie1.externalId).thenReturn("externalId")

        `when`(mockMovie2.assetExternalId).thenReturn("assetExternalId2")
        `when`(mockMovie2.description).thenReturn("description2")
        `when`(mockMovie2.name).thenReturn("name2")
        `when`(mockMovie2.prLevel).thenReturn(2)
        `when`(mockMovie2.duration).thenReturn(200)
        `when`(mockMovie2.externalId).thenReturn("externalId2")


    }

    @Test
    fun `get movies`() {
        `when`(mockMoviesRepository.getMoviesList()).thenReturn(mockLiveDataResourceMovies)
        viewModel.getMovies()
        Assert.assertNotNull(viewModel.movies)
        Assert.assertEquals(viewModel.movies, mockLiveDataResourceMovies)
    }

    @Test
    fun `search movies find`() {
        (viewModel.movies as MutableLiveData).value = Resource.success(MovieListResponse("", listOf(mockMovie1,mockMovie2)))
        viewModel.moviesFull = MovieListResponse("", listOf(mockMovie1,mockMovie2))
        viewModel.search("name2")
        viewModel.movies.observeForever{
            Assert.assertTrue(it.data!!.response.size == 1)
            Assert.assertEquals(it.data!!.response[0], mockMovie2)
        }
    }

    @Test
    fun `search movies not find`() {
        (viewModel.movies as MutableLiveData).value = Resource.success(MovieListResponse("", listOf(mockMovie1,mockMovie2)))
        viewModel.moviesFull = MovieListResponse("", listOf(mockMovie1,mockMovie2))
        viewModel.search("name false")
        viewModel.movies.observeForever{
            Assert.assertTrue(it.data!!.response.isEmpty())
        }
    }
}