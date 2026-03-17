package com.abov.moviehub.presentation.util

import androidx.core.text.HtmlCompat

fun String?.orFallback(fallback: String): String =
    if (isNullOrBlank()) fallback else this

fun Double?.toDisplayRating(fallback: String): String =
    this?.let { "%.1f".format(it) } ?: fallback

fun String?.toDisplayHtml(fallback: CharSequence = ""): CharSequence =
    this?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY).trim() } ?: fallback

