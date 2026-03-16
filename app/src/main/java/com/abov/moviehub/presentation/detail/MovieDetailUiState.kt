package com.abov.moviehub.presentation.detail

import com.abov.moviehub.domain.model.Movie

data class MovieDetailUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val movie: Movie? = null
)
