package com.example.rickandmorty.viewmodel

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.MainActivity
import com.example.rickandmorty.api.ApplicationApi
import com.example.rickandmorty.api.models.characterdetailsresponse.CharactersResponse
import com.example.rickandmorty.api.models.locationresponse.LocationList
import com.example.rickandmorty.api.models.locationresponse.LocationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RickMortyViewModel:ViewModel() {
    var _locationList= MutableLiveData<List<LocationList>>()
    val locationList:LiveData<List<LocationList>> get() = _locationList
    var locationListData= mutableListOf<LocationList>()
    var nextLocation:String?=null
    fun loadLocations(){
        viewModelScope.launch(Dispatchers.IO) {
            val callApi=ApplicationApi.retrofitService.getLocations()
            callApi.enqueue(object : Callback<LocationResponse>{
                override fun onResponse(
                    call: Call<LocationResponse>,
                    response: Response<LocationResponse>
                ) {
                    if (response.code()==200){
                        nextLocation=response.body()!!.info.next
                        locationListData += response.body()!!.results.toMutableList()
                        _locationList.value=locationListData
                        Log.e("failed",response.body()!!.results.toString())
                    }
                }
                override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                    Log.e("failed","failed")
                }
            })
        }
    }
    fun loadNextPage(){
        if(nextLocation!=null){
            viewModelScope.launch(Dispatchers.IO) {
                val callApi=ApplicationApi.retrofitService.getNextLocation(nextLocation!!)
                callApi.enqueue(object : Callback<LocationResponse>{
                    override fun onResponse(
                        call: Call<LocationResponse>,
                        response: Response<LocationResponse>
                    ) {
                        if(response.code()==200){
                            nextLocation=response.body()!!.info.next
                            locationListData+= response.body()!!.results.toMutableList()
                            _locationList.value=locationListData
                        }
                    }
                    override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                            Log.e("Failed","failed")
                    }

                })

            }
        }

    }
}