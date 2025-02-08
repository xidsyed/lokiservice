package org.example.project.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.flow.Flow
import org.example.project.presentation.ObserveAsEvents
import org.example.project.presentation.SnackbarHandler

@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier,
    state: PaymentScreenState,
    snackbarHandler: SnackbarHandler,
    uiEventsFlow: Flow<String>
) {

    ObserveAsEvents(uiEventsFlow) {
        snackbarHandler(
            message = it,
            actionLabel = null,
            duration = SnackbarDuration.Short,
            action = null
        )
    }

    if (state.paymentStarted) {
        Popup(alignment = Alignment.Center) {
            Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.05f))) {
                CircularProgressIndicator(Modifier.align(Alignment.Center))
            }
        }
    }

    if (state.checkoutSuccessful) {
        Popup(alignment = Alignment.Center) {
            Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.8f))) {
                Text(
                    "Waiting for payment confirmation ...",
                    Modifier.align(Alignment.Center),
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.W600
                )
            }
        }
    }


    Column(
        modifier.fillMaxSize().padding(horizontal = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterVertically)
    ) {

        Text(modifier = Modifier.fillMaxWidth(), text = "Pay ${state.name}")
        TextField(
            value = state.amountText.let { it.ifBlank { null } } ?: "",
            onValueChange = state.onTextChange,
            placeholder = { Text(text = "Please enter an amount") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            )
        )

        Button(
            onClick = state.onCheckout,
            enabled = state.amountText.isNotBlank() &&
                    state.amountText.toIntOrNull() != null &&
                    state.amountText.toInt() > 0 &&
                    !state.paymentStarted
        ) {
            Text("Checkout")
        }
    }
}