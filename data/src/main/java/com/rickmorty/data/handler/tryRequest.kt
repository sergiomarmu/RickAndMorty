package com.rickmorty.data.handler

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import retrofit2.Response
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException
import kotlin.coroutines.cancellation.CancellationException

/**
 * Executes a network request, handling various exceptions and errors.
 *
 * Returns the response body if successful, or throws specific network-related exceptions.
 * Handles Cancellation, IO errors, and Serialization issues with appropriate exceptions.
 */
@Throws(DataException.Network::class, DataException.Unexpected::class)
suspend inline fun <reified T> tryRequest(
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline request: suspend () -> Response<T>
): T = try {
    try {
        withContext(ioDispatcher) { request() }.let {
            if (it.isSuccessful) it.body() ?: when {
                null is T ->
                    null as T

                else -> throw DataException
                    .Network.Unparseable(
                        message = "Unexpected null body"
                    )
            } else throw DataException
                .Network.Unexpected(
                    message = "Failed response"
                )
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: IOException) {
        throw when (e) {
            is UnknownHostException -> DataException
                .Network.Unreachable(
                    message = "Socket could not be established",
                    cause = e
                )

            is SocketException -> DataException
                .Network.Unreachable(
                    message = "Socket failed",
                    cause = e
                )

            is SocketTimeoutException -> DataException
                .Network.Unreachable(
                    message = "No internet connection"
                )

            is SSLException -> DataException
                .Network.Unreachable(
                    message = "Socket security error",
                    cause = e
                )

            else -> DataException
                .Network.Unexpected(
                    message = "Unknown IO error",
                    cause = e
                )
        }
    } catch (e: SerializationException) {
        throw DataException
            .Network.Unparseable(
                message = "Could not parse server response",
                cause = e
            )
    }
} catch (e: Throwable) {
    throw e
}