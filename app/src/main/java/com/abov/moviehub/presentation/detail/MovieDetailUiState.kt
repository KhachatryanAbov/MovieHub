package com.abov.moviehub.presentation.detail

import com.abov.moviehub.domain.model.Movie

sealed class MovieDetailUiState {
    data object Loading : MovieDetailUiState()
    data class Success(val movie: Movie) : MovieDetailUiState()
    data class Error(val throwable: Throwable) : MovieDetailUiState()
}
