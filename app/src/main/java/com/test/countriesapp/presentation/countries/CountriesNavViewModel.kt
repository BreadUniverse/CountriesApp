package com.test.countriesapp.presentation.countries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.countriesapp.database.CountriesDao
import com.test.countriesapp.model.CountriesEntity
import com.test.countriesapp.model.CountriesModel
import com.test.countriesapp.retrofit.CountryApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CountriesNavViewModel(
    private val countiesDao: CountriesDao,
    private val countryApi: CountryApi
) : ViewModel() {

//    private val _screenState = MutableStateFlow(
//        ScreenState(
//            countriesList = emptyList(),
//            isErrorVisibility = false
//        )
//    )
//    val screenState: StateFlow<ScreenState> = _screenState

    val countries = countiesDao.getCountriesFlow()
        .stateIn(
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = listOf(),
            scope = viewModelScope
        )

    private val _selectedCountriesEntity = MutableStateFlow<CountriesEntity?>(null)
    val selectedCountriesEntity = _selectedCountriesEntity.asStateFlow()

    init {
        viewModelScope.launch {

            if (countiesDao.getCountries().isEmpty()) {
                loadData()
            }

//            val countriesEntities = countiesDao.getCountries()
//
//            _screenState.update { state ->
//                state.copy(
//                    countriesList = countriesEntities
//                )
//            }
        }
    }

    private suspend fun loadData() {
        val countries = runCatching { countryApi.getCountries() }
        when (countries.isSuccess) {
            true -> {
                val listInsert = countries.getOrNull()?.map {
                    it.toCountriesEntity()
                } ?: emptyList()
                countiesDao.insert(listInsert)
            }
            else -> Unit
        }
    }

    fun onCountryClick(id: Long) {
        viewModelScope.launch {
            val country = countries.value
                .find { countriesEntity -> countriesEntity.id == id }
                ?: return@launch

            _selectedCountriesEntity.update { country }
        }
    }

    fun onCheckCountry(id: Long, isChecked: Boolean) {
        viewModelScope.launch {
            val country = countries.value
                .find { countriesEntity -> countriesEntity.id == id }
                ?: return@launch
            if (country.isCheck == isChecked) return@launch
            countiesDao.update(country.copy(isCheck = isChecked))
        }
    }

    //TODO: написать сохранение комментария, разделить ViewModel
   /* fun onSaveComment(id: Long, comment: String) {
        viewModelScope.launch {
            val databaseComment = countries.value
                .find { countriesEntity -> countriesEntity.id == id }
            if (databaseComment == null) {
                countiesDao.insert(CountriesEntity(node))
            } else {
                countiesDao.update(databaseComment.copy(node = comment))
            }
        }
   */ }

    private fun CountriesModel.toCountriesEntity(): CountriesEntity {
        return CountriesEntity(
            id = 0,
            name = name.common + " (" + name.official + ")",
            population = population,
            flagImage = flags.png,
            node = "",
            isCheck = false,
            capital = capital?.firstOrNull()
            /*capital = capital,
            languages = languages*/
        )
    }



