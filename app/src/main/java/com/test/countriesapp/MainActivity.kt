package com.test.countriesapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import com.google.android.material.snackbar.Snackbar
import com.test.countriesapp.database.AppRoomDatabase
import com.test.countriesapp.model.universities.UniversitiesEntity


class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
        //val settingsItem = menu?.findItem(R.id.action_settings)
    }
}

