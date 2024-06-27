package com.example.todoapp.addtasks.ui

import com.example.todoapp.addtasks.ui.model.TaskModel
// Estados de  la UI
sealed interface TasksUIState {
    data object Loading : TasksUIState
    data class Error(val throwable: Throwable) : TasksUIState
    data class Success(val tasks: List<TaskModel>) : TasksUIState
}