package com.abov.moviehub.domain.usecase

import com.abov.moviehub.domain.model.Movie
import com.abov.moviehub.domain.repository.MovieRepository

class GetMovieDetailUseCase(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(id: Int): Movie = repository.getMovieDetail(id)
}
