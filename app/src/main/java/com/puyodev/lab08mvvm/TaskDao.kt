package com.puyodev.lab08mvvm
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface TaskDao {


    // Obtener todas las tareas
    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<Task>


    // Insertar una nueva tarea
    @Insert
    suspend fun insertTask(task: Task)


    // Marcar una tarea como completada o no completada
    @Update
    suspend fun updateTask(task: Task)


    // Eliminar todas las tareas
    @Query("DELETE FROM tasks")
    suspend fun deleteAllTasks()

    // Eliminar tarea por ID
    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Int)

    //Ordenar tareas
    @Query("SELECT * FROM tasks ORDER BY description ASC")
    suspend fun getTasksOrderedByName(): List<Task>

    @Query("SELECT * FROM tasks ORDER BY id ASC")
    suspend fun getTasksOrderedByDate(): List<Task>
}
