package org.example.project.payments.checkout.internal.context

import android.annotation.SuppressLint
import android.content.Context

class ContextProvider( val context: Context) {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: ContextProvider? = null

        fun create(context: Context): ContextProvider {
            if (instance == null) {
                instance = ContextProvider(context)
            }

            return instance!!
        }

        fun getInstance(): ContextProvider = instance
            ?: throw IllegalStateException("ContextProvider has not been initialized")
    }
}


