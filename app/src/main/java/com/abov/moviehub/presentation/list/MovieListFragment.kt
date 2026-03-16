package com.abov.moviehub.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.abov.moviehub.databinding.FragmentMovieListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListFragment : Fragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieListViewModel by viewModels()
    private lateinit var movieAdapter: MoviePagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupRecyclerView()
        setupListeners()
        observeViewModel()
        observeLoadStates()
    }

    private fun setupAdapter() {
        movieAdapter = MoviePagingAdapter { movie ->
            val directions =
                MovieListFragmentDirections.actionMovieListFragmentToMovieDetailFragment(
                    movieId = movie.id,
                    imageUrl = movie.imageMediumUrl
                )
            findNavController().navigate(directions)
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerShows.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = movieAdapter
        }
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            movieAdapter.refresh()
        }

        binding.buttonRetry.setOnClickListener {
            movieAdapter.retry()
        }
    }

    private fun observeViewModel() {
        viewModel.shows.observe(viewLifecycleOwner) { pagingData ->
            viewLifecycleOwner.lifecycleScope.launch {
                movieAdapter.submitData(pagingData)
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            renderUiState(state)
        }
    }

    private fun observeLoadStates() {
        movieAdapter.addLoadStateListener { loadState ->
            val isRefreshing = loadState.refresh is LoadState.Loading
            val isEmpty = loadState.refresh is LoadState.NotLoading && movieAdapter.itemCount == 0
            val errorState = (loadState.refresh as? LoadState.Error)
                ?: (loadState.append as? LoadState.Error)

            binding.swipeRefresh.isRefreshing = isRefreshing

            when {
                isRefreshing && movieAdapter.itemCount == 0 -> viewModel.onLoading()
                errorState != null && movieAdapter.itemCount == 0 -> {
                    viewModel.onError(errorState.error.localizedMessage ?: "Something went wrong")
                }
                isEmpty -> viewModel.onLoaded(isEmpty = true)
                else -> viewModel.onLoaded(isEmpty = false)
            }
        }
    }

    private fun renderUiState(state: MovieListUiState) = with(binding) {
        progressBar.isVisible = state.isLoading
        layoutError.isVisible = state.errorMessage != null
        textError.text = state.errorMessage ?: ""
        textEmpty.isVisible = state.isEmpty
        recyclerShows.isVisible = !state.isLoading && state.errorMessage == null && !state.isEmpty
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
