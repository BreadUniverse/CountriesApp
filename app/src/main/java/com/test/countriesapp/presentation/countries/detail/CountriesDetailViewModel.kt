package com.test.countriesapp.presentation.countries.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.countriesapp.database.CountriesDao
import com.test.countriesapp.database.universities.UniversitiesDao
import com.test.countriesapp.model.CountriesEntity
import com.test.countriesapp.model.universities.UniversitiesEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CountriesDetailViewModel(
    private val countriesDao: CountriesDao
) : ViewModel() {

    private val selectedCountriesEntity = MutableStateFlow<CountriesEntity?>(null)

    val uiState =
        selectedCountriesEntity.map { selectedCountriesEntity ->
            CountriesDetailUiState(
                countriesEntity = selectedCountriesEntity
            )
        }
        .stateIn(
            initialValue = CountriesDetailUiState(),
            started = SharingStarted.WhileSubscribed(5_000),
            scope = viewModelScope
        )

    fun onCheckCountry(isChecked: Boolean) {
        viewModelScope.launch {
            val countryId = selectedCountriesEntity.value?.id
                ?: return@launch
            countriesDao.updateIsCheckById(countryId, isChecked)
        }
    }

    fun onCommentTextFieldChanged(text: String) {
        viewModelScope.launch {
            val countryId = selectedCountriesEntity.value?.id ?: return@launch
            countriesDao.updateCommentById(countryId, text)
        }
    }

    fun getCountry(id: Long?) {
        viewModelScope.launch {
            val countryById = countriesDao.getCountriesById(id ?: -1)

            selectedCountriesEntity.update { countryById }
        }
    }
}

data class CountriesDetailUiState(
    val countriesEntity: CountriesEntity? = null
)
