package com.abov.moviehub.presentation.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.abov.moviehub.R
import com.abov.moviehub.databinding.ItemMovieBinding
import com.abov.moviehub.domain.model.Movie

class MoviePagingAdapter(
    private val onMovieClick: (Movie) -> Unit
) : PagingDataAdapter<Movie, MoviePagingAdapter.MovieViewHolder>(MOVIE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding, onMovieClick)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class MovieViewHolder(
        private val binding: ItemMovieBinding,
        private val onMovieClick: (Movie) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.root.setOnClickListener { onMovieClick(movie) }
            binding.imagePoster.load(movie.imageMediumUrl ?: movie.imageOriginalUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_placeholder)
                error(R.drawable.ic_placeholder)
            }
            binding.textTitle.text = movie.name
            binding.textRating.text = movie.rating?.let { "%.1f".format(it) }
                ?: binding.root.context.getString(R.string.common_not_available)
            binding.textPremiered.text = movie.premiered
                ?: binding.root.context.getString(R.string.common_not_available)
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
