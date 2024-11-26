package com.android.marvel.app.utils

import androidx.lifecycle.ViewModel
import com.android.marvel.app.api.ApiErrorHandler
import com.android.marvel.app.model.ErrorModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

abstract class BaseViewModel : ViewModel() {

    val scope = CoroutineScope(
        Job() + Dispatchers.Main
    )

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

    fun getError(e: Exception) : ErrorModel {
        return ApiErrorHandler.traceError(e)
    }

}