package com.example.rickandmorty.api.models.locationresponse

data class Info(
    val count: Int,
    val next: String?,
    val pages: Int,
    val prev: String?
)