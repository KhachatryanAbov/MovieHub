package com.abov.moviehub.data.remote.dto

data class MovieDto(
    val id: Int,
    val name: String,
    val language: String?,
    val genres: List<String>?,
    val status: String?,
    val premiered: String?,
    val network: NetworkDto?,
    val image: ImageDto?,
    val rating: RatingDto?,
    val summary: String?
)

data class NetworkDto(
    val id: Int?,
    val name: String?
)

data class ImageDto(
    val medium: String?,
    val original: String?
)

data class RatingDto(
    val average: Double?
)
