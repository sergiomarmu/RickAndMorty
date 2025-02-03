package com.rickmorty.data.handler

sealed class DataException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause) {

    class Unexpected(
        message: String? = null,
        cause: Throwable? = null
    ) : DataException(message, cause)

    sealed class Network(
        message: String? = null,
        cause: Throwable? = null,
    ) : DataException(
        message = message,
        cause = cause
    ) {
        class Unreachable(
            message: String? = null,
            cause: Throwable? = null
        ) : Network(message, cause)

        class Unparseable(
            message: String? = null,
            cause: Throwable? = null,
        ) : Network(message, cause)

        class Unexpected(
            message: String? = null,
            cause: Throwable? = null,
        ) : Network(message, cause)
    }
}