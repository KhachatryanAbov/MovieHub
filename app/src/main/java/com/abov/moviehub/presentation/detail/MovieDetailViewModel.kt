package com.abov.moviehub.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abov.moviehub.domain.usecase.GetMovieDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<MovieDetailUiState>(MovieDetailUiState())
    val uiState: LiveData<MovieDetailUiState> = _uiState

    fun loadMovie(id: Int) {
        _uiState.value = _uiState.value?.copy(
            isLoading = true,
            errorMessage = null
        )

        viewModelScope.launch {
            try {
                val movie = getMovieDetailUseCase(id)
                _uiState.value = MovieDetailUiState(
                    isLoading = false,
                    errorMessage = null,
                    movie = movie
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value?.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Unable to load movie details"
                )
            }
        }
    }
}
