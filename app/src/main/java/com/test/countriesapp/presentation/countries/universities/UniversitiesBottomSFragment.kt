package com.test.countriesapp.presentation.countries.universities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.test.countriesapp.databinding.BottomSheetDialogUniversitiesBinding
import com.test.countriesapp.ui.adapter.UniversitiesAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UniversitiesBottomSFragment: BottomSheetDialogFragment() {

    private val viewModel by viewModel<UniversitiesBottomSViewModel>()
    private var binding: BottomSheetDialogUniversitiesBinding? = null
    private val adapter: UniversitiesAdapter by lazy { UniversitiesAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetDialogUniversitiesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getLong("id")
        viewModel.getCountry(id)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(lifecycle).collect {
                handleUiState(it)
            }

        }
        setupUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun handleUiState(uiState: UniversitiesBottomSUIState) {

        binding?.run {

            countryName.setText(uiState.countryName)
            adapter.submitList(uiState.universities)
            loading.visibility  = if (uiState.isLoading) View.VISIBLE else View.GONE
            error.visibility = if (uiState.hasError) View.VISIBLE else View.GONE
            error.text = uiState.error
        }
    }

    private fun setupUi() {
        binding?.run {
            universitiesList.layoutManager = LinearLayoutManager(context)
            universitiesList.adapter = adapter
        }
    }
}