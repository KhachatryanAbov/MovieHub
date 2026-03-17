package com.abov.moviehub.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abov.moviehub.domain.usecase.GetMovieDetailUseCase
import com.abov.moviehub.presentation.util.toDisplayHtml
import kotlinx.coroutines.Job
import kotlinx.coroutines.CancellationException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<MovieDetailUiState>(MovieDetailUiState.Loading)
    val uiState: LiveData<MovieDetailUiState> = _uiState

    private var loadJob: Job? = null

    fun loadMovie(id: Int) {
        loadJob?.cancel()
        _uiState.value = MovieDetailUiState.Loading

        loadJob = viewModelScope.launch {
            getMovieDetailUseCase(id).fold(
                onSuccess = { movie ->
                    _uiState.value = MovieDetailUiState.Success(
                        movie = movie,
                        displaySummary = movie.summary.toDisplayHtml()
                    )
                },
                onFailure = { throwable ->
                    if (throwable is CancellationException) return@launch
                    _uiState.value = MovieDetailUiState.Error(throwable)
                }
            )
        }
    }
}
