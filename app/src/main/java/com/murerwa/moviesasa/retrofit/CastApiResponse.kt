package com.murerwa.moviesasa.retrofit

import com.murerwa.moviesasa.models.Cast
import com.murerwa.moviesasa.models.Crew

data class CastApiResponse(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)