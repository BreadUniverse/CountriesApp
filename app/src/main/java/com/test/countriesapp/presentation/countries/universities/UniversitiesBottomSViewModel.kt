package com.test.countriesapp.presentation.countries.universities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.countriesapp.database.CountriesDao
import com.test.countriesapp.database.universities.UniversitiesDao
import com.test.countriesapp.model.CountriesEntity
import com.test.countriesapp.model.universities.UniversitiesEntity
import com.test.countriesapp.model.universities.UniversitiesModel
import com.test.countriesapp.presentation.countries.toUniversitiesEntity
import com.test.countriesapp.retrofit.UniversityApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UniversitiesBottomSViewModel(
    private val countriesDao: CountriesDao,
    private val universityApi: UniversityApi,
    private val universitiesDao: UniversitiesDao
): ViewModel() {

    private fun loadUniversities(countryName: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = true)
            }

            val universitiesDb = universitiesDao.findAllByCountryName(countryName)

            if (universitiesDb.isEmpty()) {
                val universities = runCatching { universityApi.getUniversities(countryName) }

                when (universities.isSuccess) {
                    true -> {
                        val listInsert = universities.getOrNull()
                            ?.map { it.toUniversitiesEntity() } ?: emptyList()
                        universitiesDao.insert(listInsert)

                        _uiState.update {
                            it.copy(universities = listInsert)
                        }
                    }
                    else -> {
                        _uiState.update {
                            it.copy(hasError = true, error = "Ошибка соединения")
                        }
                    }
                }
            }
            else {
                _uiState.update {
                    it.copy(universities = universitiesDb)
                }
            }

            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

    private val _uiState = MutableStateFlow(UniversitiesBottomSUIState())
    val uiState = _uiState.asStateFlow()


    fun getCountry(id: Long?) {
        viewModelScope.launch {
            val countryById = countriesDao.getCountriesById(id ?: -1)
            countryById.nameCommon
            _uiState.update {
                it.copy(countryName = countryById.displayName)
            }
            loadUniversities(countryById.nameCommon)
        }
    }
}

data class UniversitiesBottomSUIState (
    val universities: List<UniversitiesEntity> = listOf(),
    val countryName: String = "",
    val isLoading: Boolean = false,
    val error: String = "",
    val hasError: Boolean = false

)