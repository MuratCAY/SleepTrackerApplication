package com.muratcay.sleeptrackerapplication.sleepdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muratcay.sleeptrackerapplication.database.SleepDatabaseDao
import com.muratcay.sleeptrackerapplication.database.SleepNight

class SleepDetailViewModel(private val sleepNightKey: Long = 0L, database: SleepDatabaseDao) :
    ViewModel() {

    private val night = MediatorLiveData<SleepNight>()

    fun getNight() = night

    private val mutableNavigateToSleepTracker: MutableLiveData<Boolean?> = MutableLiveData()
    val navigateToSleepTracker: LiveData<Boolean?>
        get() = mutableNavigateToSleepTracker

    init {
        night.addSource(database.getNightWithId(sleepNightKey), night::setValue)
    }

    fun doneNavigating(){
        mutableNavigateToSleepTracker.value = null
    }

    fun onClose(){
        mutableNavigateToSleepTracker.value = true
    }
}