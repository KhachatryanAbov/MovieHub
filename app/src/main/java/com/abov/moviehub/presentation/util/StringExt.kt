package com.abov.moviehub.presentation.util

fun String?.orFallback(fallback: String): String =
    if (isNullOrBlank()) fallback else this

