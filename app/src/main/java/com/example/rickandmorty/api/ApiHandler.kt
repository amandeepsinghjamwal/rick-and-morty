package com.example.rickandmorty.api

import com.example.rickandmorty.api.models.characterdetailsresponse.CharactersResponse
import com.example.rickandmorty.api.models.locationresponse.LocationResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url


private const val BASE_URL = "https://rickandmortyapi.com/api/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()
interface ApiHandler {
    @GET("location")
     fun getLocations():Call<LocationResponse>

    @GET
     fun getCharacters(@Url url: String):Call<CharactersResponse>

    @GET
    fun getNextLocation(@Url url: String):Call<LocationResponse>
}
object ApplicationApi{
    val retrofitService: ApiHandler by lazy { retrofit.create(ApiHandler::class.java) }
}