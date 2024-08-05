package com.test.countriesapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountriesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val nameOfficial: String,
    val nameCommon: String,
    val population: Long,
    val flagImage: String,
    var node: String,
    val isCheck: Boolean,
    val capital: String?
) {
    // TODO: Move to UI model in future for displaying UiState
    val displayName: String
        get() = "$nameCommon ($nameOfficial)"
}




