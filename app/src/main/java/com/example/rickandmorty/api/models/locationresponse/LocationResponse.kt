package com.example.rickandmorty.api.models.locationresponse

data class LocationResponse(
    val info: Info,
    val results: List<LocationList>
)