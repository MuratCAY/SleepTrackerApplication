package com.muratcay.sleeptrackerapplication.sleeptracker

import android.app.Application
import androidx.lifecycle.*
import com.muratcay.sleeptrackerapplication.database.SleepDatabaseDao
import com.muratcay.sleeptrackerapplication.database.SleepNight
import com.muratcay.sleeptrackerapplication.formatNights
import kotlinx.coroutines.launch

class SleepTrackerViewModel(val database: SleepDatabaseDao, application: Application) :
    AndroidViewModel(application) {

    private var tonight = MutableLiveData<SleepNight?>()

    private val nights = database.getAllNights() // Tüm geceler gelir.

    val nightsString = Transformations.map(nights) { nights ->
        formatNights(nights, application.resources)
    }

    val startButtonVisible = Transformations.map(tonight) {
        null == it
    }

    val stopButtonVisible = Transformations.map(tonight) {
        null != it
    }

    val clearButtonVisible = Transformations.map(nights) {
        it?.isNotEmpty()
    }

    private var mutableShowSnackBarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent: LiveData<Boolean>
        get() = mutableShowSnackBarEvent

    private val mutableNavigateToSleepQuality = MutableLiveData<SleepNight?>()
    val navigateToSleepQuality: LiveData<SleepNight?>
        get() = mutableNavigateToSleepQuality

    fun doneShowingSnackBar(){
        mutableShowSnackBarEvent.value = false
    }

    fun doneNavigating() {
        mutableNavigateToSleepQuality.value = null
    }

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        viewModelScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
        var night = database.getTonight() // Veritabanına kaydedilen en son geceyi döndürür.
        if (night?.endTimeMilli != night?.startTimeMilli) {
            night = null
        }
        return night
    }

    fun onStartTracking() {
        viewModelScope.launch {
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun insert(night: SleepNight) {
        database.insert(night)
    }

    fun onStopTracking() {
        viewModelScope.launch {
            val oldNight = tonight.value ?: return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)
            mutableNavigateToSleepQuality.value = oldNight
        }
    }

    private suspend fun update(night: SleepNight) {
        database.update(night)
    }

    fun onClear() {
        viewModelScope.launch {
            clear()
            tonight.value = null
            mutableShowSnackBarEvent.value = true
        }
    }

    private suspend fun clear() {
        database.clear()
    }
}