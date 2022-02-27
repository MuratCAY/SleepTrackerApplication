package com.muratcay.sleeptrackerapplication.sleeptracker

import com.muratcay.sleeptrackerapplication.database.SleepNight

sealed class DataItem {
    data class SleepNightItem(val sleepNight: SleepNight) : DataItem() {
        override val id: Long = sleepNight.nightId
    }

    object HeaderItem : DataItem() {
        override val id: Long = Long.MIN_VALUE
    }

    abstract val id: Long
}