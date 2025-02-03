package org.example.project

import android.app.Application
import org.example.project.loki.core.InternalCompassApi
import org.example.project.loki.permission.internal.activity.ActivityProvider
import org.example.project.loki.permission.internal.context.ContextProvider

class LocatorServiceApp : Application() {
    @InternalCompassApi
    override fun onCreate() {
        super.onCreate()
        ActivityProvider.create(context = applicationContext)
        ContextProvider.create(context = applicationContext)
    }
}