package org.example.project.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.example.project.Logger
import org.example.project.payments.checkout.model.CheckoutPayload
import org.example.project.payments.manager.PaymentManager
import org.example.project.payments.manager.createTest
import org.example.project.payments.manager.model.PaymentStatus


data class PaymentScreenState(
    val amountText: String = "",
    val name: String = "MixedWash",
    val onTextChange: (String) -> Unit,
    val onCheckout: () -> Unit,
    val paymentStarted: Boolean = false,
    val checkoutSuccessful: Boolean = false
)

class PaymentScreenViewModel(
) : ViewModel() {

    private val _state = MutableStateFlow(
        PaymentScreenState(
            onTextChange = ::onTextChange,
            onCheckout = ::onCheckout
        )
    )
    val state = _state.asStateFlow()
    private val _uiEventsChannel = Channel<String>(onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val uiEventsFlow = _uiEventsChannel.receiveAsFlow().conflate()

    val paymentManager = PaymentManager.createTest(viewModelScope)

    init {
        viewModelScope.launch {
            paymentManager.statusFlow.stateIn(
                scope = this,
                started = SharingStarted.WhileSubscribed(),
                initialValue = PaymentStatus.IDLE
            ).collect { status ->
                Logger.d("TAG", "payment status : $status")
                if (status is PaymentStatus.PaymentError) {
                    _uiEventsChannel.send(status.issue.value)
                }
                if (status is PaymentStatus.ConfirmationSuccess) {
                    _uiEventsChannel.send("Payment Successful!!!")
                }

                _state.value =
                    _state.value.copy(paymentStarted = status is PaymentStatus.Initialized, checkoutSuccessful = status is PaymentStatus.CheckoutSuccessful)
            }
        }


    }

    fun onCheckout() {
        paymentManager.checkout(
            payload = CheckoutPayload.defaultPayload(
                amount = state.value.amountText.toInt(),
                name = state.value.name,
                customerName = "Jack",
                customerEmail = "jack@gmail.com",
                customerPhoneNumber = "+91 1234567890",
                defaultMethod = null
            )
        )
    }

    fun onTextChange(text: String) {
        _state.value = _state.value.copy(amountText = text)
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PaymentScreenViewModel()
            }
        }
    }
}