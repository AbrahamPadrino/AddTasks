package com.example.todoapp.addtasks.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.todoapp.addtasks.ui.model.TaskModel


@Composable
fun TasksScreen(tasksViewModel: TasksViewModel) {  // pasar el taskViewModel: TasksViewModel

    val showDialog:Boolean by tasksViewModel.showDialog.observeAsState(initial = false) //crea instancia a liveData

    Box(modifier = Modifier.fillMaxSize()) {

       AddTaskDialog(showDialog, onDismiss = {tasksViewModel.onDialogClose()}, onTaskAdded = {tasksViewModel.onTaskAdded(it)})
       FabDialog(Modifier.align(Alignment.BottomEnd), tasksViewModel)
        TasksList(tasksViewModel)

    }
}

@Composable
fun TasksList(tasksViewModel: TasksViewModel) {
    val myTasks: List<TaskModel> = tasksViewModel.tasksList
    LazyColumn {
        items(myTasks, key = { it.id }) { task ->

            ItemTask(task, tasksViewModel)

            }
        }
    }

@Composable
fun ItemTask(taskModel : TaskModel, tasksViewModel: TasksViewModel) {
    Card(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .pointerInput(Unit) {
                                detectTapGestures(onLongPress = {
                                    tasksViewModel.onItemRemove(taskModel)
                                })
            }

        ,
        border = BorderStroke(2.dp, Color.DarkGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = taskModel.task, modifier = Modifier.weight(1f).padding(horizontal = 4.dp))
            Checkbox(checked = taskModel.selected, onCheckedChange = {tasksViewModel.onCheckBoxSelected(taskModel)})
        }
    }

}

@Composable
fun FabDialog(modifier: Modifier, tasksViewModel: TasksViewModel) {
    FloatingActionButton(
        onClick = {
            tasksViewModel.onShowDialogClick()
        },
        modifier = modifier
            .padding(16.dp)
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = "")
    }

}

@Composable
fun AddTaskDialog(show: Boolean, onDismiss: () -> Unit, onTaskAdded: (String) -> Unit) {
    var myTask by rememberSaveable { mutableStateOf("") }
    if(show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Add Task", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.size(16.dp))
                TextField(value = myTask, onValueChange = { myTask = it }, singleLine = true, maxLines = 1)
                Spacer(modifier = Modifier.size(16.dp))
                Button(
                    onClick = {
                        onTaskAdded(myTask) //mandar la tarea
                        myTask = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Add", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}