package com.test.countriesapp.di

import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.test.countriesapp.database.AppRoomDatabase
import com.test.countriesapp.database.CountriesDao
import com.test.countriesapp.database.universities.UniversitiesDao
import com.test.countriesapp.retrofit.CountryApi
import com.test.countriesapp.presentation.countries.CountriesNavViewModel
import com.test.countriesapp.presentation.countries.detail.CountriesDetailViewModel
import com.test.countriesapp.presentation.countries.universities.UniversitiesBottomSViewModel
import com.test.countriesapp.retrofit.UniversityApi
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    single<AppRoomDatabase> { //это специальная функция Koin, которая создает и хранит единственный экземпляр зависимости на протяжении жизненного цикла приложения.
        Room.databaseBuilder( //метод для создания экземпляра Room базы данных.
            androidApplication(), // функция, предоставляемая Koin, для получения объекта контекста приложения.
            AppRoomDatabase::class.java, // класс, представляющий базу данных Room.
            "country.db" //название базы данных
        ).fallbackToDestructiveMigration().build()
    }

    single<CountriesDao> {
        get<AppRoomDatabase>().countriesDao() //получение DAO объекта для доступа к операциям с базой данных
    }

    single<UniversitiesDao> {
        get<AppRoomDatabase>().universitiesDao() //получение DAO объекта для доступа к операциям с базой данных
    }

    single<Gson> { GsonBuilder().setLenient().create() }

    single<Retrofit>(named("universityApiRetrofit")) {
        Retrofit.Builder()
            .baseUrl("http://universities.hipolabs.com/")
            .addConverterFactory(GsonConverterFactory.create(get<Gson>()))
            .build()
    }

    single<UniversityApi> {
        get<Retrofit>(qualifier = named("universityApiRetrofit"))
            .create(UniversityApi::class.java)
    }

    single<Retrofit>(named("defaultRetrofit")) {
        //Создает новый экземпляр Retrofit Builder для настройки Retrofit.
        Retrofit.Builder()
            //Устанавливает базовый URL для всех запросов к API
            .baseUrl("https://restcountries.com/")
            //Добавляет конвертер Gson для работы с JSON
            // нужен, чтоб сопостовлять данные, которые приходит с бэка в формате json с нашими объектами
            .addConverterFactory(GsonConverterFactory.create(get<Gson>()))
            .build()
    }

    single<CountryApi> {
        get<Retrofit>(qualifier = named("defaultRetrofit")).create(CountryApi::class.java)
    }

    viewModel {
        CountriesNavViewModel(
            countriesDao = get(),
            countryApi = get()
        )
    }

    viewModel {
        CountriesDetailViewModel(
            countriesDao = get()
        )
    }

    viewModel{
        UniversitiesBottomSViewModel(
            universityApi = get(),
            countriesDao = get(),
            universitiesDao = get()
        )
    }
}