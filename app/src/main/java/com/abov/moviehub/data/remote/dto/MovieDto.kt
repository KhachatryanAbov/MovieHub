package com.abov.moviehub.data.remote.dto

import com.abov.moviehub.domain.model.Movie

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
) {
    fun toDomain(): Movie = Movie(
        id = id,
        name = name,
        imageMediumUrl = image?.medium,
        imageOriginalUrl = image?.original,
        rating = rating?.average,
        summary = summary,
        genres = genres.orEmpty(),
        language = language,
        status = status,
        premiered = premiered,
        networkName = network?.name
    )
}

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
