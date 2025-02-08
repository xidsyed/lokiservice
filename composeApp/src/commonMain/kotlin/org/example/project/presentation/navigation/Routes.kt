package org.example.project.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes(val route: String) {

    @Serializable
    data object HomeScreen : Routes("home")

    @Serializable
    data object LocationScreen: Routes("location")

    @Serializable
    data object PaymentScreen: Routes("payment")
}