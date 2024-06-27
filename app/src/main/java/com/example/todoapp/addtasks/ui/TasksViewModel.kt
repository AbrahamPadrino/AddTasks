package com.example.todoapp.addtasks.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.addtasks.domein.AddTaskUseCase
import com.example.todoapp.addtasks.domein.DeleteTaskUseCase
import com.example.todoapp.addtasks.domein.GetTasksUseCase
import com.example.todoapp.addtasks.domein.UpdateTaskUseCase
import com.example.todoapp.addtasks.ui.TasksUIState.Success
import com.example.todoapp.addtasks.ui.model.TaskModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class TasksViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    getTaskUseCase: GetTasksUseCase
): ViewModel() {

    val uiState: StateFlow<TasksUIState> = getTaskUseCase().map(::Success)
        .catch { TasksUIState.Error(it) }
        .stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000), TasksUIState.Loading)

    // Crea liveData para mostrar el dialog
    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog
    //

    /*private val _tasks = mutableStateListOf<TaskModel>()
    val tasksList:List<TaskModel> = _tasks
    */

    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onTaskAdded(task: String) {
        _showDialog.value = false

        viewModelScope.launch {
            addTaskUseCase(TaskModel(task = task))
        }


    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun onCheckBoxSelected(taskModel: TaskModel) {
        // Actualizar check
       viewModelScope.launch {
           updateTaskUseCase(taskModel.copy(selected = !taskModel.selected))  // .copy copia el objeto con los valores modificados como se establecen en ()
       }
    }

    fun onItemRemove(taskModel: TaskModel) {
        // Borrar item
        viewModelScope.launch {
            deleteTaskUseCase(taskModel)
        }
    }
}