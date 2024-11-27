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

    private val _homeState = MutableLiveData<HomeState>(HomeState.Idle)
    val homeState: LiveData<HomeState> = _homeState

    private val charactersList = mutableListOf<Character>()

    init {
        getCharacters()
    }

    fun getCharacters() {
        if (_homeState.value is HomeState.Loading) return

        _homeState.value = HomeState.Loading

        scope.launch {
            try {
                val response = marvelRepository.getCharacters(limit, offset)

                charactersList.addAll(response.data.results)

                _homeState.value = HomeState.Success(charactersList.toList())

                offset += limit
            } catch (e: Exception) {
                _homeState.value = HomeState.Error(getError(e).getErrorMessage())
            }
        }
    }

    fun retry() {
        getCharacters()
    }
}