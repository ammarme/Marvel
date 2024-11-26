package com.android.marvel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.marvel.app.model.Character
import com.android.marvel.app.model.CharacterData
import com.android.marvel.app.model.CharactersResponse
import com.android.marvel.app.repo.MarvelRepository
import com.android.marvel.app.ui.search.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import kotlin.coroutines.cancellation.CancellationException

@ExperimentalCoroutinesApi
class SearchViewModelTest {

    @Mock
    lateinit var marvelRepository: MarvelRepository

    private lateinit var searchViewModel: SearchViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        searchViewModel = SearchViewModel(marvelRepository)
    }

    @Test
    fun `searchByName with valid name should update charactersLiveData`() = runTest {
        val nameStartsWith = "Spider"
        val charactersResponse = listOf(
            Character(id = "1", name = "Spider-Man", description = "A hero"),
            Character(id = "2", name = "Spider-Woman", description = "A heroine")
        )
        val apiResponse = CharactersResponse(
            data = CharacterData(results = charactersResponse)
        )

        `when`(marvelRepository.searchByName(nameStartsWith)).thenReturn(apiResponse)

        searchViewModel.searchByName(nameStartsWith)

        val characters = searchViewModel.characters.value
        assertNotNull(characters)
        assertEquals(2, characters?.size)
        assertEquals("Spider-Man", characters?.get(0)?.name)
        assertEquals("Spider-Woman", characters?.get(1)?.name)
    }

    @Test
    fun `searchByName with empty name should clear charactersLiveData`() = runTest {
        val nameStartsWith = ""

        searchViewModel.searchByName(nameStartsWith)

        val characters = searchViewModel.characters.value
        assertNotNull(characters)
        assertTrue(characters.isNullOrEmpty())
    }

    @Test
    fun `searchByName with null name should clear charactersLiveData`() = runTest {
        val nameStartsWith: String? = null

        searchViewModel.searchByName(nameStartsWith)

        val characters = searchViewModel.characters.value
        assertNotNull(characters)
        assertTrue(characters.isNullOrEmpty())
    }

    @Test
    fun `searchByName with Bad response should update errorConnectionLiveData and errorMessageLiveData`() =
        runTest {
            val nameStartsWith = "Spider"
            val errorMessage = "Bad response!"

            `when`(marvelRepository.searchByName(nameStartsWith)).thenThrow(
                RuntimeException(
                    errorMessage
                )
            )

            searchViewModel.searchByName(nameStartsWith)

            val errorConnection = searchViewModel.errorConnection.value
            val errorMessageLiveData = searchViewModel.errorMessage.value

            assertTrue(errorConnection == true)
            assertEquals(errorMessage, errorMessageLiveData)
        }

    @Test
    fun `searchByName cancellation should not update any LiveData`() = runTest {
        val nameStartsWith = "Spider"

        `when`(marvelRepository.searchByName(nameStartsWith)).thenThrow(CancellationException())

        searchViewModel.searchByName(nameStartsWith)

        val characters = searchViewModel.characters.value
        val errorConnection = searchViewModel.errorConnection.value
        val errorMessageLiveData = searchViewModel.errorMessage.value

        assertNull(characters)
        assertNull(errorConnection)
        assertNull(errorMessageLiveData)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
