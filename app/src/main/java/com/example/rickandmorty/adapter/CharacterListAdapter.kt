package com.example.rickandmorty.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.rickandmorty.R
import com.example.rickandmorty.api.models.characterdetailsresponse.CharactersResponse

import com.example.rickandmorty.databinding.VerticalItemsBinding
import com.example.rickandmorty.databinding.VerticalItemsReversedBinding
import com.example.rickandmorty.viewmodel.RickMortyViewModel

class CharacterListAdapter(var viewModel: RickMortyViewModel,private val onItemClicked:(CharactersResponse)->Unit):
    ListAdapter<CharactersResponse, RecyclerView.ViewHolder>(DiffCallBack()) {

    override fun getItemViewType(position: Int): Int {
        return if (position%2==0){
            1
        }else{
            2
        }
    }
    class VerticalItemViewHolder(var binding:VerticalItemsBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CharactersResponse){
            binding.apply {
                binding.characterImage.load(data.image)
                binding.characterName.text=data.name
                when (data.gender) {
                    "Male" -> {
                        binding.gender.setImageResource(R.drawable.male_symbol)
                    }
                    "Female" -> {
                        binding.gender.setImageResource(R.drawable.female_symbol)
                    }
                    else -> {
                        binding.gender.setImageResource(R.drawable.question_mark)
                    }
                }
            }
        }
    }
    class FlippedItemViewHolder(private var binding:VerticalItemsReversedBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CharactersResponse){
            binding.apply {
                binding.characterImage.load(data.image)
                binding.characterName.text=data.name
                if(data.gender=="Male"){
                    binding.gender.setImageResource(R.drawable.male_symbol)
                }
                else if(data.gender=="Female"){
                    binding.gender.setImageResource(R.drawable.female_symbol)
                }
                else{
                    binding.gender.setImageResource(R.drawable.question_mark)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            1->{
            FlippedItemViewHolder(VerticalItemsReversedBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
            2->{
             VerticalItemViewHolder(VerticalItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
            else -> {
                throw  IllegalArgumentException("Invalid view")
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder.itemViewType==1){
            val flippedItemViewHolder= holder as FlippedItemViewHolder
            val current=getItem(position)
            flippedItemViewHolder.itemView.setOnClickListener {
                onItemClicked(current)
            }
            flippedItemViewHolder.bind(current)
        }
        else
        {
            val current = getItem(position)
            val verticalItemViewHolder= holder as VerticalItemViewHolder
            verticalItemViewHolder.itemView.setOnClickListener {
                onItemClicked(current)
            }
            verticalItemViewHolder.bind(current)
        }
    }
}
    class DiffCallBack: DiffUtil.ItemCallback<CharactersResponse>(){
        override fun areItemsTheSame(oldItem: CharactersResponse, newItem: CharactersResponse): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: CharactersResponse, newItem: CharactersResponse): Boolean {
            return oldItem==newItem
        }
    }
/*   */