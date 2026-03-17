package com.abov.moviehub.presentation.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abov.moviehub.databinding.ItemLoadStateBinding
import androidx.annotation.StringRes

class MovieLoadStateAdapter(
    private val retry: () -> Unit,
    private val errorMessage: (Throwable) -> Int
) : LoadStateAdapter<MovieLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding =
            ItemLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding, retry, errorMessage)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    class LoadStateViewHolder(
        private val binding: ItemLoadStateBinding,
        retry: () -> Unit,
        private val errorMessage: (Throwable) -> Int
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.buttonRetry.setOnClickListener { retry() }
        }

        fun bind(loadState: LoadState) = with(binding) {
            progressBar.isVisible = loadState is LoadState.Loading
            val isError = loadState is LoadState.Error
            buttonRetry.isVisible = isError
            textError.isVisible = isError

            if (loadState is LoadState.Error) {
                @StringRes val resId = errorMessage(loadState.error)
                textError.text = binding.root.context.getString(resId)
            }
        }
    }
}