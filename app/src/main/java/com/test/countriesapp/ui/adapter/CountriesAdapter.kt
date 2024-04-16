package com.test.countriesapp.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.test.countriesapp.databinding.FragmentCountriesBinding
import com.test.countriesapp.databinding.FragmentCountriesDetailBinding
import com.test.countriesapp.databinding.ItemCardBinding
import com.test.countriesapp.model.CountriesEntity


class CountriesAdapter(
    private var dataSet: List<CountriesEntity> = listOf(),
    private val onClick: (id: Long) -> Unit,
    private val onCheck: (id: Long, isChecked: Boolean) -> Unit

) : RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {

    inner class CountryViewHolder(
        private val itemCardBinding: ItemCardBinding,
    ): RecyclerView.ViewHolder(itemCardBinding.root) {

        fun bind(countriesEntity: CountriesEntity) {
            itemCardBinding.run {
                root.setOnClickListener { onClick(countriesEntity.id) }
                nameCountry.text = countriesEntity.name
                nameCountry.isSelected = true;
                flag.load(countriesEntity.flagImage)
                countCountry.text = "Population: " + countriesEntity.population
                checkBox.isChecked = countriesEntity.isCheck
                checkBox.setOnClickListener {
                    onCheck(countriesEntity.id, countriesEntity.isCheck.not())
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCardBinding.inflate(inflater, parent, false)
        return CountryViewHolder(itemCardBinding = binding)
    }

    override fun getItemCount(): Int = dataSet.size

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    //TODO: notifyDataSetChanged() убрать, использовать ListAdapter
    fun updateData(newData: List<CountriesEntity>) {
        dataSet = newData
        notifyDataSetChanged()
    }

}

