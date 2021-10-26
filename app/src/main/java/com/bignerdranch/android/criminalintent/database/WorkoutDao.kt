package com.bignerdranch.android.criminalintent.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bignerdranch.android.criminalintent.Workout
import java.util.*

@Dao
interface WorkoutDao {

    @Query("SELECT * FROM workout")
    fun getWorkouts(): LiveData<List<Workout>>

    @Query("SELECT * FROM workout WHERE id=(:id)")
    fun getWorkouts(id: UUID): LiveData<Workout?>

    @Update
    fun updateWorkout(workout: Workout)

    @Insert
    fun addWorkout(workout: Workout)

    @Delete
    fun deleteWorkout(workout: Workout)
}