package com.example.todoapp.addtasks.data

import com.example.todoapp.addtasks.ui.model.TaskModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Repositorio es la puerta de entrada a la capa de datos
@Singleton
class TaskRepository @Inject constructor(private val taskDao: TaskDao){
    // Para Leer
    val tasks: Flow<List<TaskModel>> = // Obtiene los datos del modelo de la DB y los pasa al modelo de la UI (De @Entity a TaskModel)
        taskDao.getTasks().map { items -> items.map{TaskModel(it.id, it.task, it.selected)} }
    //
    // Para Añadir
    suspend fun add(taskModel: TaskModel) { // Añade los datos del modelo de la UI y los pasa al modelo de la DB
        taskDao.addTask(taskModel.toData())
    }
    // Para Actualizar
    suspend fun update(taskModel: TaskModel) {
        taskDao.updateTask(taskModel.toData())
    }
    // Para Borrar
    suspend fun delete(taskModel: TaskModel) {
        taskDao.deleteTask(taskModel.toData())
    }
}

// Función de Extensión para proveer el taskModel a TaskEntity
fun TaskModel.toData(): TaskEntity {
    return TaskEntity(this.id, this.task, this.selected)
}