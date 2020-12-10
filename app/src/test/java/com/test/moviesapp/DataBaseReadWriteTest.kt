package com.test.moviesapp

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.test.moviesapp.data.entities.Favorite
import com.test.moviesapp.data.local.AppDatabase
import com.test.moviesapp.data.local.FavoriteDao
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DataBaseReadWriteTest : BaseTest() {

    lateinit var favoriteDao: FavoriteDao
    lateinit var appDatabase: AppDatabase


    @Mock
    lateinit var mockFavorite: Favorite

    @Mock
    lateinit var mockFavorite2: Favorite


    override fun setup() {
        MockitoAnnotations.initMocks(this)
        val context = InstrumentationRegistry.getInstrumentation().context
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        favoriteDao = appDatabase.favoriteDao()

        Mockito.`when`(mockFavorite.assetExternalId).thenReturn("assetExternalId")
        Mockito.`when`(mockFavorite.shortDescription).thenReturn("shortDescription")
        Mockito.`when`(mockFavorite.name).thenReturn("name")
        Mockito.`when`(mockFavorite.prLevel).thenReturn(1)
        Mockito.`when`(mockFavorite.cover).thenReturn("cover")
        Mockito.`when`(mockFavorite.externalId).thenReturn("externalId")


        Mockito.`when`(mockFavorite2.assetExternalId).thenReturn("assetExternalId2")
        Mockito.`when`(mockFavorite2.shortDescription).thenReturn("shortDescription2")
        Mockito.`when`(mockFavorite2.name).thenReturn("name")
        Mockito.`when`(mockFavorite2.prLevel).thenReturn(2)
        Mockito.`when`(mockFavorite2.cover).thenReturn("cover")
        Mockito.`when`(mockFavorite2.externalId).thenReturn("externalId")

    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test save two mockfavorite and recuperate`() {
        runBlocking {
            launch(Dispatchers.IO) {
                favoriteDao.insert(mockFavorite)
                favoriteDao.insert(mockFavorite2)

                val allFavorites = favoriteDao.getAllFavorites()

                assertTrue(allFavorites?.size == 2)
                assertTrue(
                    allFavorites?.findLast { mock -> mock.assetExternalId == allFavorites.get(0).assetExternalId }?.name ==
                            mockFavorite.name
                )
                assertTrue(
                    allFavorites?.findLast { mock -> mock.assetExternalId == allFavorites.get(1).assetExternalId }?.name ==
                            mockFavorite2.name
                )
            }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test save one favorite a recuperate and check all fields`() {
        runBlocking {
            launch(Dispatchers.IO) {
                val favorite = mockFavorite

                favoriteDao.insert(favorite)

                val favoriteSearch = favoriteDao.getFavorite(favorite.assetExternalId)

                assertEquals(mockFavorite.assetExternalId, favoriteSearch?.assetExternalId)
                assertEquals(mockFavorite.name, favoriteSearch?.name)
                assertEquals(mockFavorite.shortDescription, favoriteSearch?.shortDescription)
                assertEquals(mockFavorite.prLevel, favoriteSearch?.prLevel)
                assertEquals(mockFavorite.cover, favoriteSearch?.cover)
            }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test save one favorite a recuperate and delete and check`() {
        runBlocking {
            launch(Dispatchers.IO) {
                val favorite = mockFavorite
                favoriteDao.insert(favorite)

                val favoriteSearch = favoriteDao.getFavorite(favorite.assetExternalId)
                assertEquals(mockFavorite.assetExternalId, favoriteSearch!!.assetExternalId)

                favoriteDao.deleteByExternalId(favoriteSearch.assetExternalId)

                val allFavorites = favoriteDao.getAllFavorites()

                assertTrue(allFavorites!!.isEmpty())
            }
        }
    }

}