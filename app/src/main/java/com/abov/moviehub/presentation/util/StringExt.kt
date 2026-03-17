package com.abov.moviehub.presentation.util

fun String?.orFallback(fallback: String): String =
    if (isNullOrBlank()) fallback else this

fun Double?.toDisplayRating(fallback: String): String =
    this?.let { "%.1f".format(it) } ?: fallback

