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

    private val _uiState = MutableLiveData<MovieDetailUiState>(MovieDetailUiState.Loading)
    val uiState: LiveData<MovieDetailUiState> = _uiState

    fun loadMovie(id: Int) {
        _uiState.value = MovieDetailUiState.Loading

        viewModelScope.launch {
            try {
                val movie = getMovieDetailUseCase(id)
                _uiState.value = MovieDetailUiState.Success(movie)
            } catch (e: Exception) {
                _uiState.value = MovieDetailUiState.Error(e)
            }
        }
    }
}
