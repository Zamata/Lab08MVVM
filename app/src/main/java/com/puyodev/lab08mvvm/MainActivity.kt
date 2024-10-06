package com.puyodev.lab08mvvm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.puyodev.lab08mvvm.ui.theme.Lab08MVVMTheme
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab08MVVMTheme {
                val db = Room.databaseBuilder(
                    applicationContext,
                    TaskDatabase::class.java,
                    "task_db"
                ).build()


                val taskDao = db.taskDao()
                val viewModel = TaskViewModel(taskDao)


                TaskScreen(viewModel)
            }
        }
    }
}

@Composable
fun TaskScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    var newTaskDescription by remember { mutableStateOf("") }

    // Define the background color for the entire screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFDF4EF))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Encabezado de la app
        Text(
            text = "To Do List",
            style = MaterialTheme.typography.headlineMedium
        )

        // Espacio para agregar nueva tarea
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = newTaskDescription,
            onValueChange = { newTaskDescription = it },
            label = { Text("Nueva tarea") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black.copy(alpha = 0.5f),
                unfocusedTextColor = Color.Black.copy(alpha = 0.5f),
                cursorColor = Color.Black
            )

        )

        // Botón para agregar tarea
        OutlinedButton(
            onClick = {
                if (newTaskDescription.isNotEmpty()) {
                    viewModel.addTask(newTaskDescription)
                    newTaskDescription = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Black
            )
        ) {
            Text("Agregar tarea")
        }

        // Encabezado para listado de tareas con filtros (similar a un explorador)
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            // Botón con ícono y texto para ordenar por nombre
            Button(
                onClick = { viewModel.sortTasksByName() },
                modifier = Modifier.padding(end = 8.dp), // Espacio entre botones
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black, // Fondo negro
                    contentColor = Color.White // Texto e íconos blancos
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Ordenar por nombre",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Nombre", fontSize = 12.sp)
            }

            // Botón con ícono y texto para ordenar por fecha
            Button(
                onClick = { viewModel.sortTasksByDate() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Ordenar por fecha",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Fecha", fontSize = 12.sp)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Descripción",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                "Estado",
                modifier = Modifier.weight(0.5f),
                textAlign = TextAlign.Center
            )
            Text(
                "Acciones",
                modifier = Modifier.weight(0.5f),
                textAlign = TextAlign.Center
            )
        }
        // Listado de tareas con iconos y botones cuadrados
        tasks.forEach { task ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(if (task.isCompleted) Color.Green else Color.Red) // Verde si completada, rojo si pendiente
                )
                Text(text = task.description, modifier = Modifier.weight(1f))

                // Botón para cambiar estado
                IconButton(
                    onClick = { viewModel.toggleTaskCompletion(task) },
                    modifier = Modifier.weight(0.5f)
                ) {
                    Icon(
                        imageVector = if (task.isCompleted) Icons.Default.Check else Icons.Outlined.Warning,
                        contentDescription = if (task.isCompleted) "Completada" else "Pendiente"
                    )
                }

                // Botón para eliminar
                IconButton(
                    onClick = { viewModel.deleteTask(task) },
                    modifier = Modifier.weight(0.5f)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                }
            }
        }

        // Botón para eliminar todas las tareas
        OutlinedButton(
            onClick = { viewModel.deleteAllTasks() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.Black
            )
        ) {
            Text("Eliminar todas las tareas")
        }
    }
}

