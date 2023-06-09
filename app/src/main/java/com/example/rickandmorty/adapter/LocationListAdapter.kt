package com.example.rickandmorty.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.MainActivity
import com.example.rickandmorty.R
import com.example.rickandmorty.api.models.locationresponse.LocationList
import com.example.rickandmorty.databinding.HorizontalItemsBinding
import com.example.rickandmorty.viewmodel.RickMortyViewModel

class LocationListAdapter(var viewModel: RickMortyViewModel, var context: Context,var activity: Activity, private val onItemClicked:(LocationList)->Unit):
    ListAdapter<LocationList, LocationListAdapter.ItemViewHolder>(DiffCallBack) {
    private var selectedPosition = -1
    var firstTime:Boolean=true
    inner class ItemViewHolder(var binding:HorizontalItemsBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(data: LocationList, isSelected: Boolean) {

            binding.apply {

                button.text = data.name
                button.isSelected = isSelected

                if (isSelected) {
                    button.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                    button.isEnabled=false
                }
                else {
                    button.setBackgroundColor(ContextCompat.getColor(context, R.color.grey))
                    button.isEnabled=true
                    if(data.id==1 && firstTime){
                        (activity as MainActivity).loadCharacters(viewModel.resultss[0].results[0].residents)
                        button.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
                        button.isEnabled=false
                    }
                }
                button.setOnClickListener {
                    firstTime=false
                    (activity as MainActivity).showShimmer(1)
                    if(data.residents.isEmpty()){
                        (activity as MainActivity).showShimmer(0)
                        Toast.makeText(context,"No Residents here",Toast.LENGTH_SHORT).show()
                    }

                    (activity as MainActivity).loadCharacters(data.residents)
                    if (selectedPosition != adapterPosition) {
                        selectedPosition = adapterPosition
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }
    companion object{
        private val DiffCallBack= object : DiffUtil.ItemCallback<LocationList>(){
            override fun areItemsTheSame(oldItem: LocationList, newItem: LocationList): Boolean {
                return oldItem.id==newItem.id
            }

            override fun areContentsTheSame(oldItem: LocationList, newItem: LocationList): Boolean {
                return oldItem==newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(HorizontalItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
            holder.bind(current,position==selectedPosition)
    }

}