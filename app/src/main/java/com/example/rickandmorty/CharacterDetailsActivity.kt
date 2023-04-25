package com.example.rickandmorty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import com.example.rickandmorty.databinding.ActivityCharacterDetailsBinding
import com.example.rickandmorty.databinding.VerticalItemsBinding

class CharacterDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCharacterDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCharacterDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val species=intent.getStringExtra("species")
        val image=intent.getStringExtra("image")
        val gender=intent.getStringExtra("gender")
        val origin=intent.getStringExtra("origin")
        val location=intent.getStringExtra("location")
        val episode=intent.getStringExtra("episode")
        val status=intent.getStringExtra("status")
        val created=intent.getStringExtra("created")
        val name=intent.getStringExtra("name")

        binding.name.text=name
        binding.characterGender.text=gender
        binding.characterLocation.text=location
        binding.characterImageDetail.load(image)
        binding.characterOrigin.text=origin
        binding.characterEpisodes.text=episode.toString().substring(1,episode!!.length-1)
        binding.characterSpecies.text=species
        binding.characterStatus.text=status
        binding.characterCreatedAt.text=created!!.substring(0,10)
        binding.backButton.setOnClickListener{
            finish()
        }
    }
}