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
import com.abov.moviehub.presentation.util.toUserMessage
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
            setHasFixedSize(true)
            itemAnimator = null
            layoutManager = LinearLayoutManager(requireContext())
            adapter = movieAdapter.withLoadStateFooter(
                footer = MovieLoadStateAdapter(
                    retry = { movieAdapter.retry() },
                    errorMessage = { it.toUserMessage(requireContext()) }
                )
            )
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
        viewModel.movies.observe(viewLifecycleOwner) { pagingData ->
            viewLifecycleOwner.lifecycleScope.launch {
                movieAdapter.submitData(pagingData)
            }
        }
    }

    private fun observeLoadStates() {
        movieAdapter.addLoadStateListener { loadState ->
            val refresh = loadState.source.refresh
            val itemCount = movieAdapter.itemCount

            binding.swipeRefresh.isRefreshing = refresh is LoadState.Loading

            val uiState = when {
                refresh is LoadState.Loading && itemCount == 0 ->
                    MovieListUiState.Loading

                refresh is LoadState.Error && itemCount == 0 ->
                    MovieListUiState.Error(
                        refresh.error.toUserMessage(requireContext())
                    )

                refresh is LoadState.NotLoading && itemCount == 0 ->
                    MovieListUiState.Empty

                else ->
                    MovieListUiState.Content
            }

            renderUiState(uiState)
        }
    }

    private fun renderUiState(state: MovieListUiState) = with(binding) {
        progressBar.isVisible = state is MovieListUiState.Loading
        recyclerShows.isVisible = state is MovieListUiState.Content
        textEmpty.isVisible = state is MovieListUiState.Empty
        layoutError.isVisible = state is MovieListUiState.Error

        if (state is MovieListUiState.Error) {
            textError.text = state.message
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}