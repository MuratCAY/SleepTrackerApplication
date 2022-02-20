package com.muratcay.sleeptrackerapplication.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muratcay.sleeptrackerapplication.database.SleepDatabaseDao
import kotlinx.coroutines.launch

class SleepQualityViewModel(
    private val sleepNightKey: Long = 0L,
    val database: SleepDatabaseDao
) : ViewModel() {

    private val mutableNavigateToSleepTracker = MutableLiveData<Boolean?>()
    val navigateToSleepTracker: LiveData<Boolean?>
        get() = mutableNavigateToSleepTracker

    fun doneNavigating() {
        mutableNavigateToSleepTracker.value = null
    }

    fun onSetSleepQuality(quality: Int) {
        viewModelScope.launch {
            val tonight = database.get(sleepNightKey) ?: return@launch
            tonight.sleepQuality = quality
            database.update(tonight)
        }
        mutableNavigateToSleepTracker.postValue(true)
    }
}