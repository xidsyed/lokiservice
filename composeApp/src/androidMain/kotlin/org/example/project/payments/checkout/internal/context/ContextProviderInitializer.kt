package org.example.project.payments.checkout.internal.context

import android.content.Context
import androidx.startup.Initializer
import org.example.project.payments.checkout.internal.CheckoutActivityProvider

/**
* TODO : FIGURE OUT WHYYYYYY IT WONT WORK WITH MANIFEST!!!
* */
class ContextProviderInitializer : Initializer<ContextProvider> {
    override fun create(context: Context): ContextProvider {
        return ContextProvider(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        // No dependencies on other libraries.
        return emptyList()
    }
}

/**
 * Workaround
 * */

object PaymentService {
    fun initialize(context: Context) {
        ContextProvider.create(context)
        CheckoutActivityProvider.createInstance(context)
    }
}