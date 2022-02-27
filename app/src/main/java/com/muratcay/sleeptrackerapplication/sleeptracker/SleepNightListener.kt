package com.muratcay.sleeptrackerapplication.sleeptracker

import com.muratcay.sleeptrackerapplication.database.SleepNight

class SleepNightListener(val clickListener: (sleepId: Long) -> Unit) {
    fun onClick(night: SleepNight) = clickListener(night.sleepQuality.toLong())
}