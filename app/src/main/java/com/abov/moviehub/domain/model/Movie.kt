package com.abov.moviehub.domain.model

data class Movie(
    val id: Int,
    val name: String,
    val imageMediumUrl: String?,
    val imageOriginalUrl: String?,
    val rating: Double?,
    val summary: String?,
    val language: String?,
    val premiered: String?
)
