package com.abov.moviehub.data.remote

import com.abov.moviehub.data.remote.dto.MovieDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("shows")
    suspend fun getMoviesPage(
        @Query("page") page: Int
    ): List<MovieDto>

    @GET("shows/{id}")
    suspend fun getMovieDetail(
        @Path("id") id: Int
    ): MovieDto
}
