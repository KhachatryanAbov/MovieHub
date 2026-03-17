package com.abov.moviehub.presentation.list

sealed class MovieListUiState {
    object Loading : MovieListUiState()
    object Content : MovieListUiState()
    object Empty : MovieListUiState()
    data class Error(val message: String) : MovieListUiState()
}
