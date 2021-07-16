package com.murerwa.moviesasa.models

data class CastApiResponse(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)