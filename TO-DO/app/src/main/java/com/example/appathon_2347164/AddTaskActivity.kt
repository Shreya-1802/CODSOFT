package com.example.appathon_2347164

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var taskNameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var statusEditText: AutoCompleteTextView
    private lateinit var dateEditText: TextView
    private lateinit var timeEditText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_do)

        taskNameEditText = findViewById(R.id.taskNameEditText)
        descriptionEditText = findViewById(R.id.descriptionEditText)
        statusEditText = findViewById(R.id.statusEditText)
        dateEditText = findViewById(R.id.dateEditText)
        timeEditText = findViewById(R.id.timeEditText)

        // Set up the dropdown for status
        val statusOptions = listOf("Pending", "In Progress", "Completed")
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, statusOptions)
        statusEditText.setAdapter(adapter)

        dateEditText.setOnClickListener {
            showDatePickerDialog()
        }
        timeEditText.setOnClickListener {
            showTimePickerDialog()
        }

        val submitButton: Button = findViewById(R.id.submitButton)
        val cancelButton: Button = findViewById(R.id.cancelButton)

        submitButton.setOnClickListener {
            val taskName = taskNameEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val status = statusEditText.text.toString()
            val selectedDate = dateEditText.text.toString()
            val selectedTime = timeEditText.text.toString()

            if (taskName.isNotEmpty() && description.isNotEmpty() && status.isNotEmpty() && selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
                val intent = Intent().apply {
                    putExtra("taskName", taskName)
                    putExtra("description", description)
                    putExtra("status", status)
                    putExtra("date", selectedDate)
                    putExtra("time", selectedTime)
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            dateEditText.text = "$selectedYear-${selectedMonth + 1}-$selectedDay"
        }, year, month, day)
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            timeEditText.text = String.format("%02d:%02d", selectedHour, selectedMinute)
        }, hour, minute, true)
        timePickerDialog.show()
    }
}
