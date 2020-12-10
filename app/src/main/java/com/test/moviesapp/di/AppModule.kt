package com.test.moviesapp.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.test.moviesapp.data.local.AppDatabase
import com.test.moviesapp.data.local.FavoriteDao
import com.test.moviesapp.data.remote.MovieRemoteDataSource
import com.test.moviesapp.data.remote.MoviesService
import com.test.moviesapp.data.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl("https://smarttv.orangetv.orange.es/stv/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideMovieService(retrofit: Retrofit): MoviesService =
        retrofit.create(MoviesService::class.java)

    @Singleton
    @Provides
    fun provideMovieRemoteDataSource(moviesService: MoviesService) =
        MovieRemoteDataSource(moviesService)


    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideCharacterDao(db: AppDatabase) = db.favoriteDao()

    @Singleton
    @Provides
    fun provideRepository(
        remoteDataSource: MovieRemoteDataSource,
        localDataSource: FavoriteDao
    ) =
        MovieRepository(remoteDataSource, localDataSource)

}