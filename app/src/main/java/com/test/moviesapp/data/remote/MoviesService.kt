package com.test.moviesapp.data.remote

import com.test.moviesapp.data.entities.MovieListResponse
import com.test.moviesapp.data.entities.MovieResponse
import com.test.moviesapp.data.entities.RecommendedResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {
    @GET("rtv/v1/GetUnifiedList")
    suspend fun getMovieList(
        @Query("client") client: String = "json",
        @Query("statuses") statuses: String = "published",
        @Query("definitions") definitions: String = "SD;HD;4k",
        @Query("external_category_id") externalCategoryId: String = "SED_3880",
        @Query("filter_empty_categories") filterEmptyCategories: Boolean = true
    ): Response<MovieListResponse>

    @GET("rtv/v1/GetVideo")
    suspend fun getMovie(
        @Query("client") client: String = "json",
        @Query("external_id") externalId: String
    ): Response<MovieResponse>

    @GET("reco/v1/GetVideoRecommendationList")
    suspend fun getRecommended(
        @Query("client") client: String = "json",
        @Query("type") type: String = "all",
        @Query("subscription") subscription: Boolean = false,
        @Query("filter_viewed_content") filterViewedContent: Boolean = true,
        @Query("max_results") maxResults: Int = 10,
        @Query("blend") blend: String = "ar_od_blend_2424video",
        @Query("params") params: String,
        @Query("max_pr_level") maxPRLevel: Int,
        @Query("quality") quality: String
    ): Response<RecommendedResponse>

}