package com.test.countriesapp.presentation.countries.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.countriesapp.R
import com.test.countriesapp.databinding.FragmentCountriesBinding
import com.test.countriesapp.model.CountriesEntity
import com.test.countriesapp.presentation.countries.CountriesNavViewModel
import com.test.countriesapp.presentation.countries.CountriesUIState
import com.test.countriesapp.ui.adapter.CountriesAdapter
import kotlinx.coroutines.launch
import org.koin.androidx.navigation.koinNavGraphViewModel

class CountriesFragment : Fragment(R.layout.fragment_countries) {

    private var binding: FragmentCountriesBinding? = null

    private val viewModel by koinNavGraphViewModel<CountriesNavViewModel>(R.id.nav_graph_xml)

    private val countriesAdapter: CountriesAdapter by lazy {
        CountriesAdapter(
            onClick = {
                val bundle = Bundle()
                bundle.putLong("id", it)
                findNavController()
                    .navigate(R.id.action_countriesFragment_to_countriesDetailFragment, bundle)
            },
            onCheck = { id, isCheck ->
                viewModel.onCheckCountry(id, isCheck)
            }
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountriesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(lifecycle)
                .collect { uiState -> handleUiState(uiState) }
            //viewModel.screenState.collect { state -> handleUiState(state) }
        }
    }

    private fun setupUi() {
        binding?.run {
            countries.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            countries.adapter = countriesAdapter
        }
    }

    private fun handleUiState(uiState: CountriesUIState) {
        countriesAdapter.submitList(uiState.countriesList)
        binding?.run {
            loading.visibility  = if (uiState.isLoading) View.VISIBLE else View.GONE
            error.visibility = if (uiState.hasError) View.VISIBLE else View.GONE
            error.text = uiState.error
        }

    }

}
