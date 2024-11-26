package com.android.marvel.app.api


import com.android.marvel.app.model.ErrorModel
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ApiErrorHandler {

    companion object {

        fun traceError(throwable: Throwable?): ErrorModel {
            return when (throwable) {
                is HttpException -> handleHttpException(throwable)
                is SocketTimeoutException -> ErrorModel(
                    message = throwable.message ?: "Timeout error",
                    errorStatus = ErrorModel.ErrorStatus.TIMEOUT
                )
                is IOException, is UnknownHostException -> ErrorModel(
                    message = throwable.message ?: "No connection",
                    errorStatus = ErrorModel.ErrorStatus.NO_CONNECTION
                )
                else -> ErrorModel(
                    message = "No Defined Error!",
                    errorCode = 0,
                    errorStatus = ErrorModel.ErrorStatus.BAD_RESPONSE
                )
            }
        }

        private fun handleHttpException(httpException: HttpException): ErrorModel {
            return if (httpException.code() == 401) {
                ErrorModel(
                    message = httpException.message(),
                    errorCode = httpException.code(),
                    errorStatus = ErrorModel.ErrorStatus.UNAUTHORIZED
                )
            } else {
                parseHttpError(httpException.response()?.errorBody())
            }
        }

        private fun parseHttpError(errorBody: ResponseBody?): ErrorModel {
            return try {
                ErrorModel(
                    message = errorBody?.string() ?: "Unknown HTTP error",
                    errorCode = 400,
                    errorStatus = ErrorModel.ErrorStatus.BAD_RESPONSE
                )
            } catch (exception: Throwable) {
                ErrorModel(
                    message = exception.message ?: "Error parsing HTTP response",
                    errorStatus = ErrorModel.ErrorStatus.NOT_DEFINED
                )
            }
        }
    }
}
