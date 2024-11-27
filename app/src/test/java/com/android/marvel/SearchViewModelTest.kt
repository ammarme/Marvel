package com.android.marvel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.marvel.app.model.Character
import com.android.marvel.app.model.CharacterData
import com.android.marvel.app.model.CharactersResponse
import com.android.marvel.app.repo.MarvelRepository
import com.android.marvel.app.ui.search.SearchState
import com.android.marvel.app.ui.search.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.exceptions.base.MockitoException
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SearchViewModel
    private lateinit var marvelRepository: MarvelRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        marvelRepository = mock(MarvelRepository::class.java)
        viewModel = SearchViewModel(marvelRepository)
    }

    @Test
    fun `initial state is Idle`() = runTest {
        assertEquals(SearchState.Idle, viewModel.searchState.value)
    }

    @Test
    fun `search query updates to Loading and Success state`() = runTest {
        val searchQuery = "Spider-Man"
        val mockCharacters = listOf(
            mock(Character::class.java),
            mock(Character::class.java)
        )
        val mockCharactersResponse = mock(CharactersResponse::class.java)
        val mockCharacterData = mock(CharacterData::class.java)

        `when`(marvelRepository.searchByName(searchQuery)).thenReturn(mockCharactersResponse)
        `when`(mockCharactersResponse.data).thenReturn(mockCharacterData)
        `when`(mockCharacterData.results).thenReturn(mockCharacters)

        viewModel.updateSearchQuery(searchQuery)


        advanceTimeBy(310)

        val successState = viewModel.searchState.value as SearchState.Success
        assertEquals(mockCharacters, successState.characters)

        verify(marvelRepository).searchByName(searchQuery)
    }

    @Test
    fun `search query with empty string return Bad response!`() = runTest {
        val emptyQuery = ""

        viewModel.updateSearchQuery(emptyQuery)

        advanceTimeBy(310)

        assertEquals(SearchState.Idle, viewModel.searchState.value)

        verify(marvelRepository, never()).searchByName(emptyQuery)
    }

    @Test(expected = MockitoException::class)
    fun `search query handles network error`() = runTest {
        val searchQuery = "UnknownCharacter"
        val networkError = IOException("Network connection failed")

        `when`(marvelRepository.searchByName(searchQuery)).thenThrow(networkError)

        viewModel.updateSearchQuery(searchQuery)

        advanceTimeBy(310)

        assertTrue(viewModel.searchState.value is SearchState.Error)
        val errorState = viewModel.searchState.value as SearchState.Error
        assertEquals("Network connection failed", errorState.message)

        verify(marvelRepository).searchByName(searchQuery)
    }

    @Test
    fun `multiple quick searches trigger only last search`() = runTest {
        val query1 = "Iron Man"
        val query2 = "Thor"
        val mockCharactersResponse = mock(CharactersResponse::class.java)
        val mockCharacterData = mock(CharacterData::class.java)
        val mockCharacters = listOf(mock(Character::class.java))

        `when`(marvelRepository.searchByName(query2)).thenReturn(mockCharactersResponse)
        `when`(mockCharactersResponse.data).thenReturn(mockCharacterData)
        `when`(mockCharacterData.results).thenReturn(mockCharacters)

        viewModel.updateSearchQuery(query1)
        viewModel.updateSearchQuery(query2)

        advanceTimeBy(310)

        verify(marvelRepository, times(1)).searchByName(query2)

        val successState = viewModel.searchState.value as SearchState.Success
        assertEquals(mockCharacters, successState.characters)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}