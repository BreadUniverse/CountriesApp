package com.test.countriesapp.presentation.countries.detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.test.countriesapp.R
import com.test.countriesapp.database.AppRoomDatabase
import com.test.countriesapp.databinding.FragmentCountriesDetailBinding
import com.test.countriesapp.model.CountriesEntity
import com.test.countriesapp.model.universities.UniversitiesEntity
import com.test.countriesapp.presentation.countries.CountriesNavViewModel
import com.test.countriesapp.textChanges
import com.test.countriesapp.ui.adapter.CountriesAdapter
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.navigation.koinNavGraphViewModel

class CountriesDetailFragment: Fragment() {

    private var binding: FragmentCountriesDetailBinding? = null
    private val viewModel by koinNavGraphViewModel<CountriesDetailViewModel>(R.id.nav_graph_xml)

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
            viewModel.uiState
                .flowWithLifecycle(lifecycle)
                .collect { state -> handleUiState(state) }
        }
        val id = arguments?.getLong("id")
        viewModel.getCountry(id)
        setupUi()
    }

    @OptIn(FlowPreview::class)
    private fun setupUi() {
        binding?.run {
            note.textChanges()
                .debounce(300)
                .onEach { text -> viewModel.onCommentTextFieldChanged(text.toString()) }
                .launchIn(lifecycleScope)

            toolbarAttachments.setOnMenuItemClickListener{
                when (it.itemId) {
                    R.id.action_settings -> {
                        val bundle = Bundle()
                        bundle.putLong("id", arguments?.getLong("id")?: 0L)
                        findNavController().navigate(R.id.action_countriesDetailFragment_to_universitiesBottomSFragment, bundle)
                        true
                    }
                    else -> false
                }
            }

            checkBox.setOnClickListener {
                viewModel.onCheckCountry(checkBox.isChecked)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun handleUiState(uiState: CountriesDetailUiState) {

        val countriesEntity = uiState.countriesEntity

        binding?.run {
            countriesEntity?.let { entity ->
                nameCountry.text = entity.displayName
                flag.load(countriesEntity.flagImage)
                countCountry.text = "Population: ${entity.population}"
                checkBox.isChecked = countriesEntity.isCheck
                capitalCountry.text = "Capital: ${entity.capital}"
                note.setText(entity.node)
            }
        }
    }
}