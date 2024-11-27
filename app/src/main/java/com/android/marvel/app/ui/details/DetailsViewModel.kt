package com.android.marvel.app.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.marvel.app.model.Character
import com.android.marvel.app.model.DetailItem
import com.android.marvel.app.repo.MarvelRepository
import com.android.marvel.app.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val marvelRepository: MarvelRepository
) : BaseViewModel() {
    private val _characterState: MutableLiveData<DetailsState> = MutableLiveData(DetailsState.Idle)
    val characterState: LiveData<DetailsState> = _characterState

    private val comicsLiveData: MutableLiveData<List<DetailItem>> = MutableLiveData()
    val comics: LiveData<List<DetailItem>> = comicsLiveData

    private val seriesLiveData: MutableLiveData<List<DetailItem>> = MutableLiveData()
    val series: LiveData<List<DetailItem>> = seriesLiveData

    private val eventsLiveData: MutableLiveData<List<DetailItem>> = MutableLiveData()
    val events: LiveData<List<DetailItem>> = eventsLiveData

    private val storiesLiveData: MutableLiveData<List<DetailItem>> = MutableLiveData()
    val stories: LiveData<List<DetailItem>> = storiesLiveData

    fun updateCharacter(character: Character) {
        _characterState.value = DetailsState.Success(character)
        fetchAdditionalDetails(character)
    }

    private fun fetchAdditionalDetails(character: Character) {
        try {
            getStoriesByCharacterId(character.id)
            getEventsByCharacterId(character.id)
            getComicsByCharacterId(character.id)
            getSeriesByCharacterId(character.id)
        } catch (e: Exception) {
            val message = getError(e).getErrorMessage()
            _characterState.value = DetailsState.Error(message)
        }
    }

    fun getComicsByCharacterId(id: String) {
        scope.launch {
            try {
                val comicsResult = marvelRepository.getComicsByCharacterId(id)
                val comicsListHelper = comicsResult.data.results
                    .filter { it.thumbnail != null && it.thumbnail.path.isNotBlank() }
                    .map { comic ->
                        DetailItem(
                            id = comic.id,
                            title = comic.title,
                            thumbnail = comic.thumbnail
                        )
                    }
                comicsLiveData.postValue(comicsListHelper)
            } catch (e: Exception) {
                e.printStackTrace()
                comicsLiveData.postValue(emptyList())
                val message = getError(e).getErrorMessage()
                _characterState.value = DetailsState.Error(message)
            }
        }
    }

    fun getSeriesByCharacterId(id: String) {
        scope.launch {
            try {
                val seriesResult = marvelRepository.getSeriesByCharacterId(id)
                val seriesListHelper = seriesResult.data.results
                    .filter { it.thumbnail != null && it.thumbnail.path.isNotBlank() }
                    .map { series ->
                        DetailItem(
                            id = series.id,
                            title = series.title,
                            thumbnail = series.thumbnail
                        )
                    }
                seriesLiveData.postValue(seriesListHelper)
            } catch (e: Exception) {
                e.printStackTrace()
                seriesLiveData.postValue(emptyList())
                val message = getError(e).getErrorMessage()
                _characterState.value = DetailsState.Error(message)
            }
        }
    }

    fun getEventsByCharacterId(id: String) {
        scope.launch {
            try {
                val eventsResult = marvelRepository.getEventByCharacterId(id)
                val eventsListHelper = eventsResult.data.results
                    .filter { it.thumbnail != null && it.thumbnail.path.isNotBlank() }
                    .map { event ->
                        DetailItem(
                            id = event.id,
                            title = event.title,
                            thumbnail = event.thumbnail
                        )
                    }
                eventsLiveData.postValue(eventsListHelper)
            } catch (e: Exception) {
                e.printStackTrace()
                eventsLiveData.postValue(emptyList())
                val message = getError(e).getErrorMessage()
                _characterState.value = DetailsState.Error(message)            }
        }
    }

    fun getStoriesByCharacterId(id: String) {
        scope.launch {
            try {
                val storiesResult = marvelRepository.getStoriesByCharacterId(id)
                val storiesListHelper = storiesResult.data.results
                    .filter { it.thumbnail != null && it.thumbnail.path.isNotBlank() }
                    .map { story ->
                        DetailItem(
                            id = story.id,
                            title = story.title,
                            thumbnail = story.thumbnail
                        )
                    }
                storiesLiveData.postValue(storiesListHelper)
            } catch (e: Exception) {
                e.printStackTrace()
                storiesLiveData.postValue(emptyList())
                val message = getError(e).getErrorMessage()
                _characterState.value = DetailsState.Error(message)            }
        }
    }
}