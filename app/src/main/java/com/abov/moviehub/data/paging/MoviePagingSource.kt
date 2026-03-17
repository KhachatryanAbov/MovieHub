package com.abov.moviehub.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.abov.moviehub.data.remote.ApiService
import com.abov.moviehub.data.remote.mapper.MovieMapper
import com.abov.moviehub.domain.model.Movie
import kotlinx.coroutines.CancellationException

class MoviePagingSource(
    private val apiService: ApiService
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: 0
        return try {
            val response = apiService.getMoviesPage(page).map { MovieMapper.toDomain(it) }
            val nextKey = if (response.isEmpty()) null else page + 1
            LoadResult.Page(
                data = response,
                prevKey = if (page == 0) null else page - 1,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}

