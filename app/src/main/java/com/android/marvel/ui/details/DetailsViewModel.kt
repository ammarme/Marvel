package com.android.marvel.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.marvel.ui.model.Character
import com.android.marvel.ui.model.DetailItem
import com.android.marvel.ui.repo.MarvelRepository
import com.android.marvel.ui.utils.BaseViewModel
import kotlinx.coroutines.launch

class DetailViewModel(private val marvelRepository: MarvelRepository) : BaseViewModel() {
    private val comicsLiveData: MutableLiveData<List<DetailItem>> = MutableLiveData()
    val comics:LiveData<List<DetailItem>> = comicsLiveData

    private val seriesLiveData: MutableLiveData<List<DetailItem>> = MutableLiveData()
    val series:LiveData<List<DetailItem>> = seriesLiveData

    private val eventsLiveData: MutableLiveData<List<DetailItem>> = MutableLiveData()
    val events:LiveData<List<DetailItem>> = eventsLiveData

    private val storiesLiveData: MutableLiveData<List<DetailItem>> = MutableLiveData()
    val stories:LiveData<List<DetailItem>> = storiesLiveData

    val character: MutableLiveData<Character> = MutableLiveData()

    fun loadCharacter(character: Character) {
        this.character.value = character
    }

    fun getComicsByCharacterId(id: String) {
        scope.launch {
            try {
                val comicsResult = marvelRepository.getComicsByCharacterId(id)
                val comicsListHelper = comicsResult.data.results
                    .mapNotNull { comic ->
                        comic.images.firstOrNull()?.let { image ->
                            DetailItem(
                                comic.title,
                                "${image.path}.${image.extension}"
                            )
                        }
                    }
                comicsLiveData.postValue(comicsListHelper)
            } catch (e: Exception) {
                e.printStackTrace()
                comicsLiveData.postValue(emptyList())
            }
        }
    }

    fun getSeriesByCharacterId(id: String) {
        scope.launch {
            try {
                val seriesResult = marvelRepository.getSeriesByCharacterId(id)
                val seriesListHelper = seriesResult.data.results
                    .filter { it.thumbnail.path.isNotBlank() }
                    .map { series ->
                        DetailItem(
                            series.title,
                            "${series.thumbnail.path}.${series.thumbnail.extension}"
                        )
                    }
                seriesLiveData.postValue(seriesListHelper)
            } catch (e: Exception) {
                e.printStackTrace()
                seriesLiveData.postValue(emptyList())
            }
        }
    }

    fun getEventsByCharacterId(id: String) {
        scope.launch {
            try {
                val eventsResult = marvelRepository.getEventByCharacterId(id)
                val eventsListHelper = eventsResult.data.results
                    .filter { it.thumbnail.path.isNotEmpty() }
                    .map { event ->
                        DetailItem(
                            event.title,
                            "${event.thumbnail.path}.${event.thumbnail.extension}"
                        )
                    }
                eventsLiveData.postValue(eventsListHelper)
            } catch (e: Exception) {
                e.printStackTrace()
                eventsLiveData.postValue(emptyList())
            }
        }
    }

    fun getStoriesByCharacterId(id: String) {
        scope.launch {
            try {
                val storiesResult = marvelRepository.getStoriesByCharacterId(id)
                val storiesListHelper = storiesResult.data.results
                    .filter { it.thumbnail.path.isNotEmpty() }
                    .map { story ->
                        DetailItem(
                            story.title,
                            "${story.thumbnail.path}.${story.thumbnail.extension}"
                        )
                    }
                storiesLiveData.postValue(storiesListHelper)
            } catch (e: Exception) {
                e.printStackTrace()
                storiesLiveData.postValue(emptyList())
            }
        }
    }
}

class DetailsViewModelFactory(private val repository: MarvelRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}