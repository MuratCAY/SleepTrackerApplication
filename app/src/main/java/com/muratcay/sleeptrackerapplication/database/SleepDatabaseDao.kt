package com.muratcay.sleeptrackerapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SleepDatabaseDao {

    @Insert
    suspend fun insert(night: SleepNight)

    @Delete
    suspend fun delete(night: SleepNight)

    @Update
    suspend fun update(night: SleepNight)

    @Query("SELECT * FROM daily_sleep_quality_table Where nightId = :key")
    suspend fun get(key: Long): SleepNight?

    @Query("DELETE FROM daily_sleep_quality_table")
    suspend fun clear()

    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC")
    fun getAllNights(): LiveData<List<SleepNight>>

    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")
    suspend fun getTonight(): SleepNight?

    @Query("SELECT * FROM daily_sleep_quality_table WHERE nightId = :key")
    fun getNightWithId(key: Long): LiveData<SleepNight>
}