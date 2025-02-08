package org.example.project.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.example.project.presentation.navigation.Routes
import org.example.project.presentation.screens.LocationScreen
import org.example.project.presentation.screens.LocationScreenViewModel
import org.example.project.presentation.screens.PaymentScreen
import org.example.project.presentation.screens.PaymentScreenViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        val scaffoldState =
            rememberBottomSheetScaffoldState(snackbarHostState = SnackbarHostState())
        val snackbarHandler = rememberSnackbarHandler(scaffoldState)

        val navController = rememberNavController()

        Scaffold(
            snackbarHost = {
                SnackbarHost(scaffoldState.snackbarHostState) { snackbarData ->
                    Snackbar(snackbarData)
                }
            }) { innerPadding ->

            NavHost(
                modifier = Modifier.padding(innerPadding).padding(16.dp),
                navController = navController,
                startDestination = Routes.HomeScreen.route
            ) {

                composable(Routes.HomeScreen.route) {
                    HomeScreen(navController)
                }

                composable(Routes.LocationScreen.route) {
                    val viewModel: LocationScreenViewModel =
                        viewModel(factory = LocationScreenViewModel.Factory)

                    LocationScreen(snackbarHandler, viewModel)
                }

                composable(Routes.PaymentScreen.route) {
                    val viewModel: PaymentScreenViewModel =
                        viewModel(factory = PaymentScreenViewModel.Factory)
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    PaymentScreen(state = state, snackbarHandler = snackbarHandler, uiEventsFlow = viewModel.uiEventsFlow)
                }

            }
        }
    }
}


@Composable
fun HomeScreen(navController: NavController) {
    Column(Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
        Text("Services", style = MaterialTheme.typography.displayMedium)
        Spacer(Modifier.height(32.dp))
        listOf(
            "Locator" to Routes.LocationScreen,
            "Payment" to Routes.PaymentScreen
        ).forEach { pair ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(
                            pair.second.route
                        )
                    }
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp).align(Alignment.CenterStart),
                    text = pair.first,
                    style = MaterialTheme.typography.headlineMedium,
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberSnackbarHandler(
    scaffoldState: BottomSheetScaffoldState
): SnackbarHandler {
    return remember(scaffoldState.snackbarHostState) {
        SnackbarHandler { message, actionLabel, duration, action ->
            val result = scaffoldState.snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = duration ?: SnackbarDuration.Short,
            )

            if (actionLabel == null) return@SnackbarHandler
            when (result) {
                SnackbarResult.Dismissed -> Unit
                SnackbarResult.ActionPerformed -> action?.invoke()
            }
        }
    }
}

fun interface SnackbarHandler {
    suspend operator fun invoke(
        message: String,
        actionLabel: String?,
        duration: SnackbarDuration?,
        action: (() -> Unit)?
    )
}