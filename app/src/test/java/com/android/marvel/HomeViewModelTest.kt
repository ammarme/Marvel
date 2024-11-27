package com.android.marvel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.android.marvel.app.model.Character
import com.android.marvel.app.model.CharacterData
import com.android.marvel.app.model.CharactersResponse
import com.android.marvel.app.repo.MarvelRepository
import com.android.marvel.app.ui.home.HomeState
import com.android.marvel.app.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()


    @Mock
    lateinit var marvelRepository: MarvelRepository

    private lateinit var homeStateObserver: Observer<HomeState>

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        homeViewModel = HomeViewModel(marvelRepository)
        homeStateObserver = mock()
        homeViewModel.homeState.observeForever(homeStateObserver)
    }

    @Test
    fun `test successful getCharacters call`() = runTest {
        val mockCharacters = listOf(
            Character(id = "1", name = "Iron Man"),
            Character(id = "2", name = "Spider-Man")
        )
        val mockResponse = CharactersResponse(
            data = CharacterData(results = mockCharacters)
        )

        `when`(marvelRepository.getCharacters(anyInt(), anyInt())).thenReturn(mockResponse)

        val observedStates = mutableListOf<HomeState>()
        val observer = Observer<HomeState> { state ->
            observedStates.add(state)
        }

        homeViewModel.homeState.observeForever(observer)
        homeViewModel.getCharacters()

        assertEquals(3, observedStates.size)
        assertEquals(HomeState.Loading, observedStates[1])
        assertTrue(observedStates[2] is HomeState.Success)
        assertEquals(mockCharacters, (observedStates[2] as HomeState.Success).characters)

        homeViewModel.homeState.removeObserver(observer)
    }

    @Test(expected = Exception::class)
    fun `test failed getCharacters call`() = runTest {
        val errorMessage = "Network Error"
        val exception = Exception(errorMessage)

        `when`(marvelRepository.getCharacters(anyInt(), anyInt())).thenThrow(exception)

        homeViewModel.getCharacters()

        val captor = ArgumentCaptor.forClass(HomeState::class.java)
        verify(homeStateObserver, times(2)).onChanged(captor.capture())

        assertEquals(HomeState.Loading, captor.allValues[0])
        assertTrue(captor.allValues[1] is HomeState.Error)
        assertEquals(errorMessage, (captor.allValues[1] as HomeState.Error).message)
    }

    @Test
    fun `test characters list is updated on multiple getCharacters calls`() = runTest {
        val firstPageCharacters = listOf(Character(id = "1", name = "Iron Man"))
        val secondPageCharacters = listOf(Character(id = "2", name = "Spider-Man"))
        val firstResponse = CharactersResponse(
            data = CharacterData(results = firstPageCharacters)
        )
        val secondResponse = CharactersResponse(
            data = CharacterData(results = secondPageCharacters)
        )

        `when`(marvelRepository.getCharacters(20, 1)).thenReturn(firstResponse)
        `when`(marvelRepository.getCharacters(20, 21)).thenReturn(secondResponse)

        val observedStates = mutableListOf<HomeState>()
        val observer = Observer<HomeState> { state ->
            observedStates.add(state)
        }

        homeViewModel.homeState.observeForever(observer)

        homeViewModel.getCharacters()
        homeViewModel.getCharacters()

        assertEquals(5, observedStates.size)

        assertEquals(HomeState.Loading, observedStates[1])
        assertTrue(observedStates[2] is HomeState.Success)
        assertEquals(firstPageCharacters, (observedStates[2] as HomeState.Success).characters)

        assertEquals(HomeState.Loading, observedStates[3])
        assertTrue(observedStates[4] is HomeState.Success)
        assertEquals(
            firstPageCharacters + secondPageCharacters,
            (observedStates[4] as HomeState.Success).characters
        )

        homeViewModel.homeState.removeObserver(observer)
    }

    @After
    fun tearDown() {
        homeViewModel.homeState.removeObserver(homeStateObserver)
        Dispatchers.resetMain()
    }
}