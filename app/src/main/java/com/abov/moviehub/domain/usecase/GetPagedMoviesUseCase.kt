package com.abov.moviehub.domain.usecase

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.abov.moviehub.domain.model.Movie
import com.abov.moviehub.domain.repository.MovieRepository
import javax.inject.Inject

class GetPagedMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(pageSize: Int = 20): LiveData<PagingData<Movie>> =
        repository.getMoviesPaged(pageSize)
}
