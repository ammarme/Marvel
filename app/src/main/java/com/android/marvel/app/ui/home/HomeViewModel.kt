package com.android.marvel.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.marvel.app.model.Character
import com.android.marvel.app.repo.MarvelRepository
import com.android.marvel.app.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val marvelRepository: MarvelRepository
) : BaseViewModel() {

    private var limit: Int = 20
    private var offset: Int = 1

    private val charactersLiveData = MutableLiveData<List<Character>>()
    val characters: LiveData<List<Character>> = charactersLiveData // Expose immutable LiveData

    val isLoadingLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> get() = isLoadingLiveData

    private val errorConnectionLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val errorConnection: LiveData<Boolean> get() = errorConnectionLiveData

    private val errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val errorMessage: LiveData<String> get() = errorMessageLiveData

    init {
        getCharacters()
    }

    fun getCharacters() {
        isLoadingLiveData.value = true
        scope.launch {
            try {
                val response = marvelRepository.getCharacters(limit, offset)

                val currentList = charactersLiveData.value.orEmpty()
                charactersLiveData.value = currentList + response.data.results

                offset += limit
                errorConnectionLiveData.value = false
            } catch (e: Exception) {
                errorMessageLiveData.value = getError(e).getErrorMessage()
                errorConnectionLiveData.value = true
            }
            isLoadingLiveData.value = false
        }
    }
}