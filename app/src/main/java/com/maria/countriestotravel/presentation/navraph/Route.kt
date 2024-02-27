package com.maria.countriestotravel.presentation.navraph

import androidx.navigation.NamedNavArgument

sealed class Route (
    val route: String,
    val arguments: List<NamedNavArgument> = emptyList()
){
    object HomeScreen : Route(route = "homeScreen")

    object SearchScreen : Route(route = "searchScreen")

    object BookmarkScreen : Route(route = "bookMarkScreen")

    object DetailsScreen : Route(route = "detailsScreen")

    object AppStartNavigation : Route(route = "appStartNavigation")

    object CountriesNavigation : Route(route = "countriesNavigation")
}