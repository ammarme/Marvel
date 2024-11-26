package com.android.marvel.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.marvel.ui.model.Character
import com.android.marvel.ui.repo.MarvelRepository
import com.android.marvel.ui.utils.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel(private val marvelRepository: MarvelRepository) : BaseViewModel() {

    private var limit: Int = 20
    private var offset: Int = 1
    private var listCharacterHelper: MutableList<Character> = mutableListOf()

    val listCharacters: MutableLiveData<MutableList<Character>> = MutableLiveData()
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val errorConnection: MutableLiveData<Boolean> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    init {
        getCharacters()
    }

    fun getCharacters() {
        isLoading.value = true
        scope.launch {
            try {
                val response = marvelRepository.getCharacters(limit, offset)

                listCharacterHelper.addAll(response.data.results)
                listCharacters.value = listCharacterHelper
                offset += limit
                errorConnection.value = false
            } catch (e: Exception) {
                //we can catch any error here using getError
                errorMessage.value = getError(e).getErrorMessage()
                errorConnection.value = true
            }
            isLoading.value = false
        }

    }
}

class HomeViewModelFactory(private val repository: MarvelRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
