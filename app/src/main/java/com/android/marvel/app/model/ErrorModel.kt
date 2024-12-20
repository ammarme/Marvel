package com.android.marvel.app.model

private const val NO_CONNECTION_ERROR_MESSAGE = "No connection!"
private const val BAD_RESPONSE_ERROR_MESSAGE = "Bad response!"
private const val TIME_OUT_ERROR_MESSAGE = "Time out!"
private const val EMPTY_RESPONSE_ERROR_MESSAGE = "Empty response!"
private const val NOT_DEFINED_ERROR_MESSAGE = "Not defined!"
private const val UNAUTHORIZED_ERROR_MESSAGE = "Unauthorized!"

data class ErrorModel(
    val message: String? = "",
    val errorCode: Int? = null,
    val errorStatus: ErrorStatus
) {

    fun getErrorMessage(): String = when (errorStatus) {
        ErrorStatus.NO_CONNECTION -> NO_CONNECTION_ERROR_MESSAGE
        ErrorStatus.BAD_RESPONSE -> BAD_RESPONSE_ERROR_MESSAGE
        ErrorStatus.TIMEOUT -> TIME_OUT_ERROR_MESSAGE
        ErrorStatus.EMPTY_RESPONSE -> EMPTY_RESPONSE_ERROR_MESSAGE
        ErrorStatus.NOT_DEFINED -> NOT_DEFINED_ERROR_MESSAGE
        ErrorStatus.UNAUTHORIZED -> UNAUTHORIZED_ERROR_MESSAGE
    }

    enum class ErrorStatus {
        NO_CONNECTION,
        BAD_RESPONSE,
        TIMEOUT,
        EMPTY_RESPONSE,
        NOT_DEFINED,
        UNAUTHORIZED
    }
}
