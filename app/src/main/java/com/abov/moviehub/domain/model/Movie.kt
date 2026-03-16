package com.abov.moviehub.domain.model

data class Movie(
    val id: Int,
    val name: String,
    val imageMediumUrl: String?,
    val imageOriginalUrl: String?,
    val rating: Double?,
    val summary: String?,
    val genres: List<String>,
    val language: String?,
    val status: String?,
    val premiered: String?,
    val networkName: String?
)
