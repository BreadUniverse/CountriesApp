package com.test.countriesapp.presentation.countries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.countriesapp.database.CountriesDao
import com.test.countriesapp.database.universities.UniversitiesDao
import com.test.countriesapp.model.CountriesEntity
import com.test.countriesapp.model.CountriesModel
import com.test.countriesapp.model.universities.UniversitiesEntity
import com.test.countriesapp.model.universities.UniversitiesModel
import com.test.countriesapp.retrofit.CountryApi
import com.test.countriesapp.retrofit.UniversityApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CountriesNavViewModel(
    private val countriesDao: CountriesDao,
    private val countryApi: CountryApi
) : ViewModel() {

    private val countries = countriesDao.getCountriesFlow()
        .stateIn(
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = listOf(),
            scope = viewModelScope
        )

    private val viewModelState = MutableStateFlow(CountriesVMState())
    val uiState = combine(
        countries,
        viewModelState
    ) { countries, viewModelState ->
        CountriesUIState(countriesList = countries, isLoading = viewModelState.isLoading,
            error = viewModelState.error, hasError = viewModelState.hasError)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CountriesUIState())

    init {
        viewModelScope.launch {
            if (countriesDao.getCountries().isEmpty()) {
                loadCountries()
            }
        }
    }

    private suspend fun loadCountries() {
        viewModelScope.launch {
            viewModelState.update {
                it.copy(isLoading = true)
            }

            if (countries.value.isEmpty()) {
                val countries = runCatching { countryApi.getCountries() }
                when (countries.isSuccess) {
                    true -> {
                        val listInsert = countries.getOrNull()
                            ?.map { it.toCountriesEntity() } ?: emptyList()
                        countriesDao.insert(listInsert)
                    }

                    else -> {
                        viewModelState.update {
                            it.copy(hasError = true, error = "Ошибка соединения")
                        }
                    }
                }
            }
            viewModelState.update {
                it.copy(isLoading = false)
            }
        }
    }

    fun onCheckCountry(id: Long, isChecked: Boolean) {
        viewModelScope.launch {
            countriesDao.updateIsCheckById(id, isChecked)
        }
    }
}

private fun CountriesModel.toCountriesEntity(): CountriesEntity {
    return CountriesEntity(
        id = 0,
        nameCommon = name.common,
        nameOfficial = name.official,
        population = population,
        flagImage = flags.png,
        node = "",
        isCheck = false,
        capital = capital?.firstOrNull()
    )
}

fun UniversitiesModel.toUniversitiesEntity(): UniversitiesEntity {
    return UniversitiesEntity(
        name = name,
        country = country
    )
}

data class CountriesUIState(
    val countriesList: List<CountriesEntity> = listOf(),
    val isLoading: Boolean = false,
    val error: String = "",
    val hasError: Boolean = false
)

data class CountriesVMState(
    val isLoading: Boolean = false,
    val error: String = "",
    val hasError: Boolean = false
)


