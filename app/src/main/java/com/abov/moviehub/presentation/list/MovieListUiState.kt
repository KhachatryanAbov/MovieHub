package com.abov.moviehub.presentation.list

sealed class MovieListUiState {
    data object Loading : MovieListUiState()
    data object Content : MovieListUiState()
    data object Empty : MovieListUiState()
    data class Error(val message: String) : MovieListUiState()
}
