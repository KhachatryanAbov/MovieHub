package com.abov.moviehub.presentation.detail

import com.abov.moviehub.domain.model.Movie
import androidx.annotation.StringRes

sealed class MovieDetailUiState {
    data object Loading : MovieDetailUiState()
    data class Success(val movie: Movie) : MovieDetailUiState()
    data class Error(@StringRes val messageRes: Int) : MovieDetailUiState()
}
