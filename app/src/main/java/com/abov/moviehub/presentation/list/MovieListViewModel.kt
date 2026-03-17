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

@HiltViewModel
class MovieListViewModel @Inject constructor(
    getPagedMoviesUseCase: GetPagedMoviesUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<MovieListUiState>(MovieListUiState.Loading)
    val uiState: LiveData<MovieListUiState> = _uiState

    val movies: LiveData<PagingData<Movie>> = getPagedMoviesUseCase().cachedIn(viewModelScope)

    fun onLoading() {
        _uiState.value = MovieListUiState.Loading
    }

    fun onLoaded(isEmpty: Boolean) {
        _uiState.value = if (isEmpty) MovieListUiState.Empty else MovieListUiState.Content
    }

    fun onError(message: String) {
        _uiState.value = MovieListUiState.Error(message)
    }
}
