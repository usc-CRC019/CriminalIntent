package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders

private lateinit var workout: Workout

class ConfirmDeleteFragment : DialogFragment() {

    private val workoutListViewModel: WorkoutListViewModel by lazy {
        ViewModelProviders.of(this).get(WorkoutListViewModel::class.java)
    }

    private lateinit var confirmText: TextView
    private lateinit var confirmButton: Button
    private lateinit var cancelButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_confirm, container, false)

        confirmText = view.findViewById(R.id.confirm_text)
        confirmButton = view.findViewById(R.id.confirm_button)
        cancelButton = view.findViewById(R.id.cancel_button)

        return view
    }

    override fun onStart() {
        super.onStart()

        confirmButton.setOnClickListener {
            workoutListViewModel.deleteWorkout(workout)
            val text = R.string.workout_deleted
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(context, text, duration)
            toast.show()
            activity?.onBackPressed()
            dismiss()
        }

        cancelButton.setOnClickListener {
            dismiss()
        }

    }

    companion object {
        fun newInstance(workoutId: Workout): ConfirmDeleteFragment {
            workout = workoutId
            return ConfirmDeleteFragment()
        }
    }

}