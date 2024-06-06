package br.com.fiap.mailtime.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun EmailDetailScreen(navController: NavController, subject: String, body: String, from: String, to: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "De: $from", fontSize = 18.sp)
        Text(text = "Para: $to", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Assunto: $subject", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Mensagem:", fontSize = 18.sp)
        Text(text = body, fontSize = 16.sp, modifier = Modifier.padding(top = 8.dp))

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Voltar")
        }
    }
}
