package com.android.marvel.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.marvel.ui.model.Character
import com.android.marvel.ui.repo.MarvelRepository
import com.android.marvel.ui.utils.BaseViewModel
import kotlinx.coroutines.launch

class SearchViewModel(private val marvelRepository: MarvelRepository) : BaseViewModel() {


    val listCharacters: MutableLiveData<List<Character>> = MutableLiveData()
    val errorConnection: MutableLiveData<Boolean> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    fun searchByName(nameStartsWith:String?) {
        if(nameStartsWith.isNullOrBlank()) {
            listCharacters.value = listOf()
            return
        }

        scope.launch {
            try {
                val response = marvelRepository.searchByName(nameStartsWith)

                listCharacters.value = response.data.results
                errorConnection.value = false
            } catch (e: Exception) {
                //we can catch any error here using getError
                errorMessage.value = getError(e).getErrorMessage()
                errorConnection.value = true
            }
        }

    }
}

class SearchViewModelFactory(private val repository: MarvelRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
