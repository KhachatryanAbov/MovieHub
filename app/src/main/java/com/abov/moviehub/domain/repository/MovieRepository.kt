package com.abov.moviehub.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.abov.moviehub.domain.model.Movie

interface MovieRepository {
    fun getMoviesPaged(pageSize: Int): LiveData<PagingData<Movie>>
    suspend fun getMovieDetail(id: Int): Movie
}