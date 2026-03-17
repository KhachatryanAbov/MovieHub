package com.abov.moviehub.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.text.HtmlCompat
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import coil.memory.MemoryCache
import com.abov.moviehub.R
import com.abov.moviehub.databinding.FragmentMovieDetailBinding
import com.abov.moviehub.presentation.util.orFallback
import com.abov.moviehub.presentation.util.toUserMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailViewModel by viewModels()
    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preloadThumbnail()
        setupListeners()
        observeUiState()

        if (savedInstanceState == null) {
            viewModel.loadMovie(args.movieId)
        }
    }

    private fun preloadThumbnail() {
        val thumbnailUrl = args.imageUrl ?: return
        binding.imagePoster.load(thumbnailUrl) {
            placeholderMemoryCacheKey(MemoryCache.Key(thumbnailUrl))
            error(R.drawable.ic_placeholder)
        }
    }

    private fun setupListeners() {
        binding.buttonRetry.setOnClickListener {
            viewModel.loadMovie(args.movieId)
        }
    }

    private fun observeUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            renderState(state)
        }
    }

    private fun renderState(state: MovieDetailUiState) = with(binding) {
        progressBar.isVisible = state is MovieDetailUiState.Loading
        contentScroll.isVisible = state is MovieDetailUiState.Success
        layoutError.isVisible = state is MovieDetailUiState.Error

        when (state) {
            is MovieDetailUiState.Success -> renderContent(state)
            is MovieDetailUiState.Error -> textError.text =
                state.throwable.toUserMessage(requireContext())
            else -> Unit
        }
    }

    private fun FragmentMovieDetailBinding.renderContent(state: MovieDetailUiState.Success) {
        val movie = state.movie
        val originalUrl = movie.imageOriginalUrl ?: movie.imageMediumUrl
        imagePoster.load(originalUrl) {
            args.imageUrl?.let { placeholderMemoryCacheKey(MemoryCache.Key(it)) }
            crossfade(true)
            placeholder(R.drawable.ic_placeholder)
            error(R.drawable.ic_placeholder)
        }
        textTitle.text = movie.name
        val fallback = getString(R.string.common_not_available)
        val ratingText = movie.rating?.let { "%.1f".format(it) } ?: fallback
        textRating.text = getString(R.string.movies_rating_format, ratingText)
        textLanguage.text = movie.language.orFallback(fallback)
        textPremiered.text = movie.premiered.orFallback(fallback)
        textSummary.text = movie.summary
            ?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY).trim() }
            ?: ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
