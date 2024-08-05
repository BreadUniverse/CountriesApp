package com.test.countriesapp.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.test.countriesapp.databinding.ItemCardUniversitiesBinding
import com.test.countriesapp.model.universities.UniversitiesEntity


class UniversitiesAdapter(
) : ListAdapter<UniversitiesEntity, UniversitiesAdapter.UniversitiesViewHolder>(UniversitiesDiffCallback()) {


    inner class UniversitiesViewHolder(
        private val itemCardBinding: ItemCardUniversitiesBinding
    ): RecyclerView.ViewHolder(itemCardBinding.root) {

        fun bind(universitiesEntity: UniversitiesEntity) {
            itemCardBinding.run {
              nameUniversity.setText(universitiesEntity.name)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversitiesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCardUniversitiesBinding.inflate(inflater, parent, false)
        return UniversitiesViewHolder(itemCardBinding = binding)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: UniversitiesViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}

class UniversitiesDiffCallback : DiffUtil.ItemCallback<UniversitiesEntity>() {
    override fun areItemsTheSame(oldItem: UniversitiesEntity, newItem: UniversitiesEntity): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: UniversitiesEntity, newItem: UniversitiesEntity): Boolean {
        return oldItem == newItem
    }
}

