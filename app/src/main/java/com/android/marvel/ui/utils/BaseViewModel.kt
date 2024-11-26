package com.android.marvel.ui.utils

import androidx.lifecycle.ViewModel

import androidx.lifecycle.*
import com.android.marvel.ui.api.ApiErrorHandle
import com.android.marvel.ui.model.ErrorModel

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
        return ApiErrorHandle.traceErrorException(e)
    }

    fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }

}