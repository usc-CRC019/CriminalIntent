package com.bignerdranch.android.criminalintent

import android.app.Application

class WorkoutIntentApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        WorkoutRepository.initialize(this)
    }
}