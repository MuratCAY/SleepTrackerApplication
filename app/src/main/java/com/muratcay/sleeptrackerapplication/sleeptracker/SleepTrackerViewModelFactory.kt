package com.muratcay.sleeptrackerapplication.sleeptracker

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.muratcay.sleeptrackerapplication.database.SleepDatabase
import com.muratcay.sleeptrackerapplication.database.SleepDatabaseDao

@Suppress("UNCHECKED_CAST")
class SleepTrackerViewModelFactory(private val dataSource: SleepDatabaseDao, private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepTrackerViewModel::class.java)) {
            return SleepTrackerViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }
}