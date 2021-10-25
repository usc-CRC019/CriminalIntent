package com.bignerdranch.android.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import java.util.*

private const val TAG = "WorkoutFragment"
private const val ARG_WORKOUT_ID = "workout_id"
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0

class WorkoutFragment : Fragment(), DatePickerFragment.Callbacks {

    private lateinit var workout: Workout
    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var groupCheckBox: CheckBox
    private lateinit var deleteButton: Button
    private lateinit var saveButton: Button
    private lateinit var startTimeButton: Button
    private lateinit var endTimeButton: Button
    private lateinit var placeField: EditText



    private val workoutDetailViewModel: WorkoutDetailViewModel by lazy {
        ViewModelProviders.of(this).get(WorkoutDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        workout = Workout()
        val workoutId: UUID = arguments?.getSerializable(ARG_WORKOUT_ID) as UUID
        workoutDetailViewModel.loadWorkout(workoutId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_workout, container, false)

        titleField = view.findViewById(R.id.workout_title) as EditText
        dateButton = view.findViewById(R.id.workout_date) as Button
        groupCheckBox = view.findViewById(R.id.workout_solved) as CheckBox
        deleteButton = view.findViewById(R.id.delete_button) as Button
        saveButton = view.findViewById(R.id.save_button) as Button
        startTimeButton = view.findViewById(R.id.start_button) as Button
        endTimeButton = view.findViewById(R.id.end_button) as Button
        placeField = view.findViewById(R.id.place_title)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val workoutId = arguments?.getSerializable(ARG_WORKOUT_ID) as UUID
        workoutDetailViewModel.loadWorkout(workoutId)
        workoutDetailViewModel.workoutLiveData.observe(
            viewLifecycleOwner,
            Observer { workout ->
                workout?.let {
                    this.workout = workout
                    updateUI()
                }
            })

        val appCompatActivity = activity as AppCompatActivity
        appCompatActivity.supportActionBar?.setTitle(R.string.new_workout)

    }

    override fun onStart() {
        super.onStart()

        val placeWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                workout.place = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }

        val titleWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                workout.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }
        titleField.addTextChangedListener(titleWatcher)
        placeField.addTextChangedListener(placeWatcher)

        groupCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                workout.isGrouped = isChecked
            }
        }

        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(workout.date).apply {
                setTargetFragment(this@WorkoutFragment, REQUEST_DATE)
                show(this@WorkoutFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }

        deleteButton.setOnClickListener {
            ConfirmDeleteFragment.newInstance(workout).apply {
                show(this@WorkoutFragment.requireFragmentManager(), "tag")
            }
        }

        saveButton.setOnClickListener {
            ConfirmSaveFragment.newInstance(workout).apply {
                show(this@WorkoutFragment.requireFragmentManager(), "tag")
            }
        }

        startTimeButton.setOnClickListener {
            val theTime: String
            val hour = Calendar.getInstance().get(Calendar.HOUR)
            val minutes = Calendar.getInstance().get(Calendar.MINUTE)

            theTime = if (minutes < 10 && Calendar.getInstance().get(Calendar.AM_PM) == Calendar.AM){
                "Start: $hour:0$minutes AM"
            }
            else if (minutes >= 10 && Calendar.getInstance().get(Calendar.AM_PM) == Calendar.AM) {
                "Start: $hour:$minutes AM"
            } else if (minutes < 10 && Calendar.getInstance().get(Calendar.AM_PM) == Calendar.PM){
                "Start: $hour:0$minutes PM"
            } else {
                "Start: $hour:$minutes PM"
            }
            workout.startTime = theTime
            startTimeButton.text = workout.startTime
        }

        endTimeButton.setOnClickListener {
            val theTime: String
            val hour = Calendar.getInstance().get(Calendar.HOUR)
            val minutes = Calendar.getInstance().get(Calendar.MINUTE)

            theTime = if (minutes < 10 && Calendar.getInstance().get(Calendar.AM_PM) == Calendar.AM){
                "End: $hour:0$minutes AM"
            }
            else if (minutes >= 10 && Calendar.getInstance().get(Calendar.AM_PM) == Calendar.AM) {
                "End: $hour:$minutes AM"
            } else if (minutes < 10 && Calendar.getInstance().get(Calendar.AM_PM) == Calendar.PM){
                "End: $hour:0$minutes PM"
            } else {
                "End: $hour:$minutes PM"
            }
            workout.endTime = theTime
            endTimeButton.text = workout.endTime

        }
    }

    override fun onStop() {
        super.onStop()
        workoutDetailViewModel.saveWorkout(workout)
    }

    override fun onDateSelected(date: Date) {
        workout.date = date
        updateUI()
    }

    private fun updateUI() {
        titleField.setText(workout.title)
        placeField.setText(workout.place)
        dateButton.text = workout.date.toString()
        groupCheckBox.isChecked = workout.isGrouped
        startTimeButton.text = workout.startTime
        endTimeButton.text = workout.endTime
    }

    companion object {

        fun newInstance(workoutId: UUID): WorkoutFragment {
            val args = Bundle().apply {
                putSerializable(ARG_WORKOUT_ID, workoutId)
            }
            return WorkoutFragment().apply {
                arguments = args
            }
        }
    }
}