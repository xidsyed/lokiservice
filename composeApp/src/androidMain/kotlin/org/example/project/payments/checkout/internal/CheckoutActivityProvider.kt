package org.example.project.payments.checkout.internal

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.razorpay.PaymentResultWithDataListener
import java.lang.ref.WeakReference

class CheckoutActivityProvider private constructor(context: Context) {
    private var checkoutActivityRef: WeakReference<Activity>? = null


    init {
        val application = context.applicationContext as? Application

        application?.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityResumed(activity: Activity) {
                if (activity is PaymentResultWithDataListener) {
                    checkoutActivityRef = WeakReference(activity)
                }
            }

            override fun onActivityPaused(p0: Activity) {}
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
            ?: run {
                val activity = context.applicationContext as? ComponentActivity
                if (activity != null && activity is PaymentResultWithDataListener) {
                    this.checkoutActivityRef = WeakReference(activity)
                }
            }
    }

    companion object {
        @Volatile
        private var instance: CheckoutActivityProvider? = null
        fun createInstance(context: Context) {
            instance ?: synchronized(this) {
                instance ?: CheckoutActivityProvider(context).also { instance = it }
            }
        }

        fun getCheckoutActivity(): Activity {
            try {
                return instance!!.checkoutActivityRef!!.get()!!
            } catch (e: Exception) {
                throw IllegalStateException("CheckoutActivity is not available")
            }
        }
    }
}