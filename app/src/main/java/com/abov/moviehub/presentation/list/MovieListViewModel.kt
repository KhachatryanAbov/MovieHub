package com.abov.moviehub.presentation.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.abov.moviehub.domain.model.Movie
import com.abov.moviehub.domain.usecase.GetPagedMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

data class MovieListUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isEmpty: Boolean = false
)

@HiltViewModel
class MovieListViewModel @Inject constructor(
    getPagedMoviesUseCase: GetPagedMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData(MovieListUiState(isLoading = true))
    val uiState: LiveData<MovieListUiState> = _uiState

    val shows: LiveData<PagingData<Movie>> =
        getPagedMoviesUseCase()
            .cachedIn(viewModelScope)

    fun onLoading() {
        _uiState.value = _uiState.value?.copy(isLoading = true, errorMessage = null)
    }

    fun onLoaded(isEmpty: Boolean) {
        _uiState.value = _uiState.value?.copy(
            isLoading = false,
            errorMessage = null,
            isEmpty = isEmpty
        )
    }

    fun onError(message: String?) {
        _uiState.value = _uiState.value?.copy(
            isLoading = false,
            errorMessage = message ?: "Something went wrong",
            isEmpty = false
        )
    }
}
