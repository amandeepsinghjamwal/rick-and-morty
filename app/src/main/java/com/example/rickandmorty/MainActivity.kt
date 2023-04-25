package com.example.rickandmorty

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.adapter.CharacterListAdapter
import com.example.rickandmorty.adapter.LocationListAdapter
import com.example.rickandmorty.api.ApplicationApi
import com.example.rickandmorty.api.models.characterdetailsresponse.CharactersResponse
import com.example.rickandmorty.databinding.ActivityMainBinding
import com.example.rickandmorty.viewmodel.RickMortyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: RickMortyViewModel
    private lateinit var characterListAdapter: CharacterListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[RickMortyViewModel::class.java]

        characterListAdapter = CharacterListAdapter(viewModel) { character ->
            val episodeOf = mutableListOf<String>()
            character.episode.forEach { episode ->
                episodeOf += episode.substring(40)
            }
            sendDetails(
                character.name,
                character.species,
                character.status,
                character.created,
                episodeOf,
                character.gender,
                character.image,
                character.location.name,
                character.origin.name
            )
        }
        binding.recyclerView.adapter = characterListAdapter

        val locationListAdapter = LocationListAdapter(viewModel, this, this) {}
        binding.locationRecyclerView.adapter = locationListAdapter

        viewModel.locationList.observe(this) { location ->
            locationListAdapter.submitList(location)
        }

        binding.locationRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    viewModel.loadNextPage()
                    Log.e("reached","triggered")
                    locationListAdapter.notifyDataSetChanged()
                }
            }
        })
        viewModel.loadLocations()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.locationRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun sendDetails(
        name: String,
        species: String,
        status: String,
        created: String,
        episode: List<String>,
        gender: String,
        image: String,
        location: String,
        origin: String
    ) {
        intent = Intent(this, CharacterDetailsActivity::class.java).apply {
            putExtra("name", name)
            putExtra("species", species)
            putExtra("status", status)
            putExtra("created", created)
            putExtra("episode", episode.toString())
            putExtra("gender", gender)
            putExtra("image", image)
            putExtra("location", location)
            putExtra("origin", origin)
        }
        startActivity(intent)
    }
    fun loadCharacters(residents:List<String>){
        val responseCharacter = emptyList<CharactersResponse>().toMutableList()
        characterListAdapter.submitList(responseCharacter)
        CoroutineScope(Dispatchers.IO).launch {
            for(resident in residents){
                val callApi= ApplicationApi.retrofitService.getCharacters(resident)
                callApi.enqueue(object : Callback<CharactersResponse> {
                    override fun onResponse(
                        call: Call<CharactersResponse>,
                        response: Response<CharactersResponse>
                    ) {
                        Log.e("heree",response.code().toString())
                        if(response.code()==200){
                            responseCharacter += response.body()!!
                            characterListAdapter.notifyDataSetChanged()
                        }
                    }
                    override fun onFailure(call: Call<CharactersResponse>, t: Throwable) {
                        Log.e("faild","failed")
                    }
                })
            }
            Log.e("faild","failed")
        }
    }
}
