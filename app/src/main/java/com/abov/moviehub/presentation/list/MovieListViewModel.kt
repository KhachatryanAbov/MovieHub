package com.abov.moviehub.presentation.list

import androidx.lifecycle.LiveData
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

    val movies: LiveData<PagingData<Movie>> =
        getPagedMoviesUseCase().cachedIn(viewModelScope)
}