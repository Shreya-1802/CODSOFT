package com.example.appathon_2347164

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskListAdapter(
    private val taskItems: MutableList<TaskItem>,
    private val clickListener: TaskItemClickListener
) : RecyclerView.Adapter<TaskListAdapter.TaskViewHolder>() {

    interface TaskItemClickListener {
        fun onEditClick(position: Int)
        fun onDeleteClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.task_item_layout, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val taskItem = taskItems[position]
        holder.taskNameTextView.text = taskItem.taskName
        holder.descriptionTextView.text = taskItem.description
        holder.statusTextView.text = taskItem.status
        holder.dateTextView.text = taskItem.date
        holder.timeTextView.text = taskItem.time

        holder.editButton.setOnClickListener {
            clickListener.onEditClick(position)
        }

        holder.deleteButton.setOnClickListener {
            clickListener.onDeleteClick(position)
        }
    }

    override fun getItemCount(): Int {
        return taskItems.size
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskNameTextView: TextView = itemView.findViewById(R.id.taskNameTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val editButton: Button = itemView.findViewById(R.id.editButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }
}
