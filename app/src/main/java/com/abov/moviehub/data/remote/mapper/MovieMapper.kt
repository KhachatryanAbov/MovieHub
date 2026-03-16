package com.abov.moviehub.data.remote.mapper

import com.abov.moviehub.data.remote.dto.MovieDto
import com.abov.moviehub.domain.model.Movie

object MovieMapper {

    fun toDomain(dto: MovieDto): Movie = Movie(
        id = dto.id,
        name = dto.name,
        imageMediumUrl = dto.image?.medium,
        imageOriginalUrl = dto.image?.original,
        rating = dto.rating?.average,
        summary = dto.summary,
        genres = dto.genres.orEmpty(),
        language = dto.language,
        status = dto.status,
        premiered = dto.premiered,
        networkName = dto.network?.name
    )
}
