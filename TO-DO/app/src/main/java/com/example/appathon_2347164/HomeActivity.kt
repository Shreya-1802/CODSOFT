package com.example.appathon_2347164

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HomeActivity : AppCompatActivity(), TaskListAdapter.TaskItemClickListener {

    private lateinit var adapter: TaskListAdapter
    private val taskItems = mutableListOf<TaskItem>()
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        Log.d(TAG, "onCreate: HomeActivity started")

        sharedPreferences = getSharedPreferences("TaskPrefs", Context.MODE_PRIVATE)

        // Load task items from SharedPreferences
        loadTaskItems()

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_item1 -> {
                    // Avoid unnecessary recreation of HomeActivity
                    if (false) {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    }
                    true
                }

                R.id.nav_item3 -> {
                    Toast.makeText(this, "This is the Task list Page", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.nav_exit -> {
                    AlertDialog.Builder(this)
                        .setMessage("Are you sure you want to exit?")
                        .setPositiveButton("Yes") { dialog, which ->
                            finishAffinity()
                        }
                        .setNegativeButton("No") { dialog, which ->
                            dialog.dismiss()
                        }
                        .show()
                    true
                }

                else -> false
            }
        }
        /*  navigationView.setNavigationItemSelectedListener { menuItem ->
              when (menuItem.itemId) {
                  R.id.nav_item1 -> {
                      // Avoid unnecessary recreation of HomeActivity
                      if (false) {
                          val intent = Intent(this, HomeActivity::class.java)
                          startActivity(intent)
                      }
                      true
                  }

                  R.id.nav_item3 -> {
                      Toast.makeText(this, "This is the Task list Page", Toast.LENGTH_SHORT).show()
                      true
                  }

                  R.id.nav_exit -> {
                      AlertDialog.Builder(this)
                          .setMessage("Are you sure you want to exit?")
                          .setPositiveButton("Yes") { dialog, which ->
                              finishAffinity()
                          }
                          .setNegativeButton("No") { dialog, which ->
                              dialog.dismiss()
                          }
                          .show()
                      true
                  }

                  else -> false
              }
          }*/
        // RecyclerView setup and adapter initialization
        val taskListRecyclerView: RecyclerView = findViewById(R.id.taskListRecyclerView)
        adapter = TaskListAdapter(taskItems, this)
        taskListRecyclerView.adapter = adapter
        taskListRecyclerView.layoutManager = LinearLayoutManager(this)

        // Floating Action Button setup
        val addButton: FloatingActionButton = findViewById(R.id.addListingFab)
        addButton.setOnClickListener {
            Log.d(TAG, "Add button clicked")
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivityForResult(intent, ADD_TASK_REQUEST_CODE)
        }
    }

    companion object {
        const val ADD_TASK_REQUEST_CODE = 1001
        const val EDIT_TASK_REQUEST_CODE = 1002
        const val TASK_POSITION_EXTRA = "task_position"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == RESULT_OK) {
            val taskName = data?.getStringExtra("taskName") ?: ""
            val description = data?.getStringExtra("description") ?: ""
            val status = data?.getStringExtra("status") ?: ""
            val date = data?.getStringExtra("date") ?: ""
            val time = data?.getStringExtra("time") ?: ""
            val newTaskItem = TaskItem(taskName, description, status, date, time)
            taskItems.add(newTaskItem)
            adapter.notifyDataSetChanged()
            saveTaskItems()
        } else if (requestCode == EDIT_TASK_REQUEST_CODE && resultCode == RESULT_OK) {
            val position = data?.getIntExtra(TASK_POSITION_EXTRA, -1) ?: -1
            if (position != -1) {
                val taskName = data?.getStringExtra("taskName") ?: ""
                val description = data?.getStringExtra("description") ?: ""
                val status = data?.getStringExtra("status") ?: ""
                val date = data?.getStringExtra("date") ?: ""
                val time = data?.getStringExtra("time") ?: ""
                taskItems[position] = TaskItem(taskName, description, status, date, time)
                adapter.notifyItemChanged(position)
                saveTaskItems()
            }
        }
    }

    override fun onEditClick(position: Int) {
        editTask(position)
    }

    private fun editTask(position: Int) {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra(TASK_POSITION_EXTRA, position)
        intent.putExtra("taskName", taskItems[position].taskName)
        intent.putExtra("description", taskItems[position].description)
        intent.putExtra("status", taskItems[position].status)
        intent.putExtra("date", taskItems[position].date)
        intent.putExtra("time", taskItems[position].time)
        startActivityForResult(intent, EDIT_TASK_REQUEST_CODE)
    }

    override fun onDeleteClick(position: Int) {
        showDeleteDialog(position)
    }

    private fun showDeleteDialog(position: Int) {
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to delete this task?")
            .setPositiveButton("Yes") { dialog, which ->
                deleteTask(position)
            }
            .setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteTask(position: Int) {
        taskItems.removeAt(position)
        adapter.notifyDataSetChanged()
        saveTaskItems()
    }

    private fun saveTaskItems() {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(taskItems)
        editor.putString("taskItems", json)
        editor.apply()
    }

    private fun loadTaskItems() {
        val json = sharedPreferences.getString("taskItems", null)
        if (json != null) {
            val type = object : TypeToken<List<TaskItem>>() {}.type
            val items: List<TaskItem> = gson.fromJson(json, type)
            taskItems.clear()
            taskItems.addAll(items)
        }
    }
}
