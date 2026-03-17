package com.abov.moviehub.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abov.moviehub.domain.usecase.GetMovieDetailUseCase
import com.abov.moviehub.presentation.util.toUserMessage
import kotlinx.coroutines.Job
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMovieDetailUseCase: GetMovieDetailUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<MovieDetailUiState>(MovieDetailUiState.Loading)
    val uiState: LiveData<MovieDetailUiState> = _uiState

    private var loadJob: Job? = null

    private val movieId: Int = checkNotNull(savedStateHandle[MOVIE_ID_KEY])

    init {
        loadMovie()
    }

    fun retry() {
        loadMovie()
    }

    private fun loadMovie() {
        loadJob?.cancel()
        _uiState.value = MovieDetailUiState.Loading

        loadJob = viewModelScope.launch {
            getMovieDetailUseCase(movieId).fold(
                onSuccess = { movie ->
                    _uiState.value = MovieDetailUiState.Success(movie)
                },
                onFailure = { throwable ->
                    _uiState.value = MovieDetailUiState.Error(throwable.toUserMessage())
                }
            )
        }
    }

    private companion object {
        const val MOVIE_ID_KEY = "movieId"
    }
}
