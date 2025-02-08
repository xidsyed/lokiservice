package org.example.project.payments.manager.model

import org.example.project.payments.manager.model.PaymentStatus.PaymentError.Issue

data class PaymentException(val throwable: Throwable, val issue: Issue) :
    Exception() {
    override fun toString(): String {
        return "${issue.value} : ${throwable.message}"
    }
}