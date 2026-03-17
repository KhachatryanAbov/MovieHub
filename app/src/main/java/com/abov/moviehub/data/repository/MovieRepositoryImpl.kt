package com.abov.moviehub.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.abov.moviehub.data.remote.ApiService
import com.abov.moviehub.data.remote.mapper.MovieMapper
import com.abov.moviehub.data.remote.paging.MoviePagingSource
import com.abov.moviehub.domain.model.Movie
import com.abov.moviehub.domain.repository.MovieRepository

class MovieRepositoryImpl(
    private val apiService: ApiService
) : MovieRepository {

    override fun getMoviesPaged(pageSize: Int) =
        Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(apiService) }
        ).liveData

    override suspend fun getMovieDetail(id: Int): Movie =
        MovieMapper.toDomain(apiService.getMovieDetail(id))
}
