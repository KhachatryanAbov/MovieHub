package com.abov.moviehub.presentation.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import coil3.request.error
import coil3.request.placeholder
import com.abov.moviehub.R
import com.abov.moviehub.databinding.ItemMovieBinding
import com.abov.moviehub.domain.model.Movie
import com.abov.moviehub.presentation.util.orFallback
import com.abov.moviehub.presentation.util.toDisplayRating

class MoviePagingAdapter(
    private val onMovieClick: (Movie) -> Unit
) : PagingDataAdapter<Movie, MoviePagingAdapter.MovieViewHolder>(MOVIE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding, onMovieClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let(holder::bind)
    }

    class MovieViewHolder(
        private val binding: ItemMovieBinding,
        private val onMovieClick: (Movie) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private var currentMovie: Movie? = null

        init {
            binding.root.setOnClickListener {
                currentMovie?.let(onMovieClick)
            }
        }

        fun bind(movie: Movie) {
            currentMovie = movie

            binding.imagePoster.load(movie.imageMediumUrl ?: movie.imageOriginalUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_placeholder)
                error(R.drawable.ic_placeholder)
            }

            binding.textTitle.text = movie.name

            val ctx = binding.root.context
            val fallback = ctx.getString(R.string.common_not_available)
            val ratingText = movie.rating.toDisplayRating(fallback)
            val premieredText = movie.premiered.orFallback(fallback)

            binding.textRating.text =
                ctx.getString(R.string.movies_rating_format, ratingText)
            binding.textPremiered.text =
                ctx.getString(R.string.movies_premiered_format, premieredText)
        }
    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
                oldItem == newItem
        }
    }
}