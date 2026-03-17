package com.abov.moviehub.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.abov.moviehub.data.paging.MoviePagingSource
import com.abov.moviehub.data.remote.ApiService
import com.abov.moviehub.data.remote.mapper.MovieMapper
import com.abov.moviehub.domain.model.Movie
import com.abov.moviehub.domain.repository.MovieRepository
import kotlinx.coroutines.CancellationException

class MovieRepositoryImpl(
    private val apiService: ApiService
) : MovieRepository {

    override fun getMoviesPaged(pageSize: Int) =
        Pager(
            config = PagingConfig(
                pageSize = pageSize,
                prefetchDistance = pageSize / 2,
                initialLoadSize = pageSize,
                maxSize = pageSize * 5,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MoviePagingSource(apiService) }
        ).liveData

    override suspend fun getMovieDetail(id: Int): Result<Movie> = runCatching {
        MovieMapper.toDomain(apiService.getMovieDetail(id))
    }.onFailure { throwable ->
        if (throwable is CancellationException) throw throwable
    }
}
