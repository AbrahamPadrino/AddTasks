package com.example.todoapp.addtasks.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.todoapp.addtasks.ui.model.TaskModel
import javax.inject.Inject

class TasksViewModel @Inject constructor(): ViewModel() {

    // Crea liveData para mostrar el dialog
    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog
    //

    private val _tasks = mutableStateListOf<TaskModel>()
    val tasksList:List<TaskModel> = _tasks


    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onTaskAdded(task: String) {
        _showDialog.value = false

        _tasks.add(TaskModel(task = task))


    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun onCheckBoxSelected(taskModel: TaskModel) {
        val index = _tasks.indexOf(taskModel)
        _tasks[index] = _tasks[index].let {
            it.copy( selected = !it.selected)
        }

    }

    fun onItemRemove(taskModel: TaskModel) {
        val task = _tasks.find { it.id == taskModel.id }
        _tasks.remove(task)

    }


}