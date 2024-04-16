package com.test.countriesapp.presentation.countries.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.test.countriesapp.R
import com.test.countriesapp.databinding.FragmentCountriesDetailBinding
import com.test.countriesapp.model.CountriesEntity
import com.test.countriesapp.presentation.countries.CountriesNavViewModel
import com.test.countriesapp.ui.adapter.CountriesAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.navigation.koinNavGraphViewModel

class CountriesDetailFragment: Fragment(
) {

    private var binding: FragmentCountriesDetailBinding? = null
    private val viewModel by koinNavGraphViewModel<CountriesNavViewModel>(R.id.nav_graph_xml)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountriesDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().popBackStack()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.selectedCountriesEntity
                .flowWithLifecycle(lifecycle)
                .collect { selectedCountry -> selectedCountry?.let { handleState(it) } }
        }
    }

    private fun handleState(countriesEntity: CountriesEntity) {
        binding?.run {
            nameCountry.text = countriesEntity.name
            nameCountry.isSelected = true;
            flag.load(countriesEntity.flagImage)
            countCountry.text = "Population: " + countriesEntity.population
            checkBox.isChecked = countriesEntity.isCheck
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}