package org.viper75.todolistmvvm.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.viper75.todolistmvvm.data.Task
import org.viper75.todolistmvvm.databinding.TaskItemBinding

class TasksListAdapter(
    private val listener: OnItemClickListener
) : ListAdapter<Task, TasksListAdapter.TaskItemViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskItemViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskItemViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    interface OnItemClickListener {
        fun onItemClick(task: Task)
        fun onItemChecked(task: Task, isChecked: Boolean)
    }

    inner class TaskItemViewHolder(
        private val binding: TaskItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    val clickedTask = getItem(position)
                    listener.onItemClick(clickedTask)
                }

                completedCheckBox.apply {
                    setOnClickListener {
                        val position = adapterPosition
                        val clickedTask = getItem(position)
                        listener.onItemChecked(clickedTask, isChecked)
                    }
                }
            }
        }

        fun bind(task: Task) {
            binding.apply {
                nameTextView.text = task.name
                nameTextView.paint.isStrikeThruText = task.completed
                completedCheckBox.isChecked = task.completed
                importantImageView.isVisible = task.important
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Task, newItem: Task) =
            oldItem == newItem
    }
}