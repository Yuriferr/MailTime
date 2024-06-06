package br.com.fiap.mailtime.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun FiltroCalendario(onDateSelected: (Date) -> Unit) {
    var currentMonth by remember { mutableStateOf(Calendar.getInstance()) }

    Column(modifier = Modifier.fillMaxWidth()) {
        NavegacaoMes(
            currentMonth = currentMonth,
            onPreviousMonth = { currentMonth.add(Calendar.MONTH, -1) },
            onNextMonth = { currentMonth.add(Calendar.MONTH, 1) }
        )
        CabecalhoDiasSemana()
        DiasDaSemana(currentMonth = currentMonth, onDateSelected = onDateSelected)
    }
}

@Composable
fun NavegacaoMes(currentMonth: Calendar, onPreviousMonth: () -> Unit, onNextMonth: () -> Unit) {
    val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Mês Anterior")
        }
        Text(text = monthFormat.format(currentMonth.time), fontSize = 20.sp)
        IconButton(onClick = onNextMonth) {
            Icon(Icons.Filled.ArrowForward, contentDescription = "Próximo Mês")
        }
    }
}

@Composable
fun CabecalhoDiasSemana() {
    val daysOfWeek = listOf("Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        daysOfWeek.forEach { day ->
            Text(text = day, fontSize = 16.sp, modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun DiasDaSemana(currentMonth: Calendar, onDateSelected: (Date) -> Unit) {
    val calendar = currentMonth.clone() as Calendar
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
        calendar.add(Calendar.DAY_OF_MONTH, -1)
    }

    Column {
        repeat(6) { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                repeat(7) { dayOfWeek ->
                    val date = calendar.time
                    Text(
                        text = calendar.get(Calendar.DAY_OF_MONTH).toString(),
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { onDateSelected(date) }
                    )
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                }
            }
        }
    }
}
