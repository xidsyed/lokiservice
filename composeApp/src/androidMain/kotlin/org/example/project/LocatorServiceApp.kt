package org.example.project

import android.app.Application
import org.example.project.loki.core.InternalLokiApi
import org.example.project.loki.permission.internal.context.LokiService
import org.example.project.payments.checkout.internal.context.PaymentService

class LocatorServiceApp : Application() {
    @InternalLokiApi
    override fun onCreate() {
        super.onCreate()
        LokiService.initialize(context = applicationContext)
        PaymentService.initialize(context = applicationContext)
    }
}