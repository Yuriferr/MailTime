package br.com.fiap.mailtime.ui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.mailtime.ui.components.FiltroCalendario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(email: String, navController: NavController) {
    var emails by remember { mutableStateOf<List<QueryDocumentSnapshot>?>(null) }
    var filteredEmails by remember { mutableStateOf<List<QueryDocumentSnapshot>?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            emails = fetchEmails(email)
            filteredEmails = filterEmailsByDate(emails, Date())
        } catch (e: Exception) {
            errorMessage = e.message
            Log.e("HomeScreen", "Erro ao buscar emails", e)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { /* Ação para abrir o menu lateral */ }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("login") {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    }) {
                        Icon(Icons.Filled.ExitToApp, contentDescription = "Deslogar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("compose/$email") }) {
                Icon(Icons.Filled.Add, contentDescription = "Compor")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            FiltroCalendario(onDateSelected = { selectedDate ->
                filteredEmails = filterEmailsByDate(emails, selectedDate)
            })
            Spacer(modifier = Modifier.height(16.dp))
            errorMessage?.let {
                Text(
                    text = "Erro: $it",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            filteredEmails?.let {
                ListaEmails(emails = it, navController = navController)
            } ?: run {
                if (errorMessage == null) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }
    }
}

@Composable
fun ListaEmails(emails: List<QueryDocumentSnapshot>, navController: NavController) {
    LazyColumn {
        items(emails) { email ->
            val subject = email.getString("subject") ?: "Sem Assunto"
            val body = email.getString("body") ?: "Sem Conteúdo"
            val from = email.getString("from") ?: "Desconhecido"
            val to = email.getString("to") ?: "Desconhecido"
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable {
                        navController.navigate("emailDetail/$subject/$body/$from/$to")
                    }
            ) {
                Text(text = subject, fontSize = 18.sp, color = Color.Black)
                Text(text = body, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

suspend fun fetchEmails(userEmail: String): List<QueryDocumentSnapshot> {
    val db = FirebaseFirestore.getInstance()
    val receivedEmails = db.collection("emails")
        .whereEqualTo("to", userEmail)
        .get()
        .await()
    val sentEmails = db.collection("emails")
        .whereEqualTo("from", userEmail)
        .get()
        .await()
    return receivedEmails.documents.map { it as QueryDocumentSnapshot } + sentEmails.documents.map { it as QueryDocumentSnapshot }
}

fun filterEmailsByDate(emails: List<QueryDocumentSnapshot>?, date: Date): List<QueryDocumentSnapshot>? {
    if (emails == null) return null
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val selectedDateString = sdf.format(date)
    return emails.filter { email ->
        val timestamp = email.getDate("timestamp")
        timestamp != null && sdf.format(timestamp) == selectedDateString
    }
}
