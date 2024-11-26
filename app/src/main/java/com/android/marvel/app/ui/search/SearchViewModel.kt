package com.android.marvel.app.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.marvel.app.model.Character
import com.android.marvel.app.repo.MarvelRepository
import com.android.marvel.app.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val marvelRepository: MarvelRepository
) : BaseViewModel() {

    private val charactersLiveData: MutableLiveData<List<Character>> = MutableLiveData()
    val characters: LiveData<List<Character>> get() = charactersLiveData

    private val errorConnectionLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val errorConnection: LiveData<Boolean> get() = errorConnectionLiveData

    private val errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String> get() = errorMessageLiveData

    private var searchJob: Job? = null

    fun searchByName(nameStartsWith: String?) {
        searchJob?.cancel()

        if (nameStartsWith.isNullOrBlank()) {
            charactersLiveData.value = emptyList()
            return
        }

        searchJob = scope.launch {
            try {
                val response = marvelRepository.searchByName(nameStartsWith)

                charactersLiveData.value = response.data.results
                errorConnectionLiveData.value = false
            } catch (_: CancellationException) {

            } catch (e: Exception) {
                errorMessageLiveData.value = getError(e).getErrorMessage()
                errorConnectionLiveData.value = true
            }
        }
    }
}

