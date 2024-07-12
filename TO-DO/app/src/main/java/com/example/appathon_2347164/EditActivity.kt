package com.example.appathon_2347164

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.util.*
import android.app.Activity
import android.content.Intent


class EditActivity : AppCompatActivity() {

    private var position: Int = -1
    private lateinit var taskNameEditText: TextInputEditText
    private lateinit var descriptionEditText: TextInputEditText
    private lateinit var statusEditText: MaterialAutoCompleteTextView
    private lateinit var dateEditText: TextInputEditText
    private lateinit var timeEditText: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit)
        taskNameEditText = findViewById(R.id.taskNameEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        statusEditText = findViewById(R.id.statusEditText)
        dateEditText = findViewById(R.id.dateEditText)
        timeEditText = findViewById(R.id.timeEditText)

        position = intent.getIntExtra(HomeActivity.TASK_POSITION_EXTRA, -1)
        val taskName = intent.getStringExtra("taskName") ?: ""
        val description = intent.getStringExtra("description") ?: ""
        val status = intent.getStringExtra("status") ?: ""
        val date = intent.getStringExtra("date") ?: ""
        val time = intent.getStringExtra("time") ?: ""

        taskNameEditText.setText(taskName)
        descriptionEditText.setText(description)
        statusEditText.setText(status)
        dateEditText.setText(date)
        timeEditText.setText(time)

        dateEditText.setOnClickListener {
            showDatePickerDialog(dateEditText)
        }

        timeEditText.setOnClickListener {
            showTimePickerDialog(timeEditText)
        }

        val submitButton = findViewById<Button>(R.id.submitButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)

        submitButton.setOnClickListener {
            val updatedTaskName = taskNameEditText.text.toString()
            val updatedDescription = descriptionEditText.text.toString()
            val updatedStatus = statusEditText.text.toString()
            val updatedDate = dateEditText.text.toString()
            val updatedTime = timeEditText.text.toString()

            val resultIntent = Intent()
            resultIntent.putExtra(HomeActivity.TASK_POSITION_EXTRA, position)
            resultIntent.putExtra("taskName", updatedTaskName)
            resultIntent.putExtra("description", updatedDescription)
            resultIntent.putExtra("status", updatedStatus)
            resultIntent.putExtra("date", updatedDate)
            resultIntent.putExtra("time", updatedTime)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        cancelButton.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                editText.setText(date)
            }, year, month, day)

        datePickerDialog.show()
    }

    private fun showTimePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val time = "$selectedHour:$selectedMinute"
            editText.setText(time)
        }, hour, minute, true)

        timePickerDialog.show()
    }
}
