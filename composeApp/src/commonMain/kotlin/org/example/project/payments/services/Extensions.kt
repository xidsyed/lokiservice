package org.example.project.payments.services

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun DefaultHttpClient(
    enableLogging: Boolean = false,
    ignoresUnknownKeys: Boolean = true,
    username: String? = null,
    password: String? = null,
    maxRetries: Int = 3
) = HttpClient {
    expectSuccess = true

    install(HttpRequestRetry) {
        retryOnServerErrors(maxRetries = maxRetries)
        exponentialDelay()
    }


    if (enableLogging) {
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    org.example.project.Logger.i("Info", message)
                }
            }
        }
    }
    if (username != null && password != null) {
        install(Auth) {
            basic {
                credentials {
                    BasicAuthCredentials(username = username, password = password)
                }
            }
        }
    }

    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = ignoresUnknownKeys
            prettyPrint = true
        })
    }
}
