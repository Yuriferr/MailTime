package br.com.fiap.mailtime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.fiap.mailtime.ui.LoginScreen
import br.com.fiap.mailtime.ui.RegisterScreen
import br.com.fiap.mailtime.ui.HomeScreen
import br.com.fiap.mailtime.ui.ComposeScreen
import br.com.fiap.mailtime.ui.EmailDetailScreen
import br.com.fiap.mailtime.ui.theme.MailTimeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MailTimeTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavGraph(navController = navController, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = "login", modifier = modifier) {
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable(
            "home/{email}",
            arguments = listOf(navArgument("email") { defaultValue = "" })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            HomeScreen(email = email, navController = navController)
        }
        composable(
            "compose/{fromEmail}",
            arguments = listOf(navArgument("fromEmail") { defaultValue = "" })
        ) { backStackEntry ->
            val fromEmail = backStackEntry.arguments?.getString("fromEmail") ?: ""
            ComposeScreen(navController = navController, fromEmail = fromEmail)
        }
        composable(
            "emailDetail/{subject}/{body}/{from}/{to}",
            arguments = listOf(
                navArgument("subject") { defaultValue = "No Subject" },
                navArgument("body") { defaultValue = "No Content" },
                navArgument("from") { defaultValue = "Unknown" },
                navArgument("to") { defaultValue = "Unknown" }
            )
        ) { backStackEntry ->
            val subject = backStackEntry.arguments?.getString("subject") ?: "No Subject"
            val body = backStackEntry.arguments?.getString("body") ?: "No Content"
            val from = backStackEntry.arguments?.getString("from") ?: "Unknown"
            val to = backStackEntry.arguments?.getString("to") ?: "Unknown"
            EmailDetailScreen(navController = navController, subject = subject, body = body, from = from, to = to)
        }
    }
}
