package com.android.marvel.app.ui.search

import com.android.marvel.app.repo.MarvelRepository
import com.android.marvel.app.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val marvelRepository: MarvelRepository
) : BaseViewModel() {

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Idle)
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()
    private val _searchQuery = MutableStateFlow("")

    init {
        setupSearchDebounce()
    }

    @OptIn(FlowPreview::class)
    private fun setupSearchDebounce() {
        scope.launch {
            _searchQuery
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    if(query.isNotBlank()) {
                        _searchState.value = SearchState.Loading
                        searchCharacters(query)
                    }
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private suspend fun searchCharacters(query: String) {
        try {
            val results = marvelRepository.searchByName(query)
            _searchState.value = SearchState.Success(results.data.results)
        } catch (e: Exception) {
            val message = getError(e).getErrorMessage()
            _searchState.value = SearchState.Error(message)
        }
    }
}

