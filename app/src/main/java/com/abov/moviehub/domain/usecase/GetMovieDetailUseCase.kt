package com.abov.moviehub.domain.usecase

import com.abov.moviehub.domain.model.Movie
import com.abov.moviehub.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(id: Int): Result<Movie> = repository.getMovieDetail(id)
}
