package com.android.marvel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.android.marvel.app.model.Character
import com.android.marvel.app.model.CharacterData
import com.android.marvel.app.model.CharactersResponse
import com.android.marvel.app.repo.MarvelRepository
import com.android.marvel.app.ui.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mock
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

    @Mock
    lateinit var charactersObserver: Observer<List<Character>>

    @Mock
    lateinit var isLoadingObserver: Observer<Boolean>

    @Mock
    lateinit var errorConnectionObserver: Observer<Boolean>

    @Mock
    lateinit var errorMessageObserver: Observer<String>

    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        homeViewModel = HomeViewModel(marvelRepository)
        homeViewModel.characters.observeForever(charactersObserver)
        homeViewModel.isLoading.observeForever(isLoadingObserver)
        homeViewModel.errorConnection.observeForever(errorConnectionObserver)
        homeViewModel.errorMessage.observeForever(errorMessageObserver)
    }

    @Test
    fun `test successful getCharacters call`() = runTest {
        val mockCharacters =
            listOf(Character(id = "1", name = "Iron Man"), Character(id = "2", name = "Spider-Man"))
        val mockResponse = CharactersResponse(
            data = CharacterData(results = mockCharacters)
        )

        `when`(marvelRepository.getCharacters(anyInt(), anyInt())).thenReturn(mockResponse)

        homeViewModel.getCharacters()

        val isLoadingCaptor = ArgumentCaptor.forClass(Boolean::class.java)
        verify(isLoadingObserver, times(3)).onChanged(isLoadingCaptor.capture())

        assertTrue(isLoadingCaptor.allValues[1])
        assertFalse(isLoadingCaptor.allValues[2])
        verify(charactersObserver).onChanged(mockCharacters)
        verify(errorConnectionObserver).onChanged(false)
    }

    @Test(expected = Exception::class)
    fun `test failed getCharacters call`() = runTest {
        val exception = Exception("Network Error")
        `when`(marvelRepository.getCharacters(anyInt(), anyInt())).thenThrow(exception)

        homeViewModel.getCharacters()

        val isLoadingCaptor = ArgumentCaptor.forClass(Boolean::class.java)
        verify(isLoadingObserver, times(3)).onChanged(isLoadingCaptor.capture())

        assertTrue(isLoadingCaptor.allValues[1])
        assertFalse(isLoadingCaptor.allValues[2])
        verify(errorConnectionObserver).onChanged(true)
        verify(errorMessageObserver).onChanged("Network Error")
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

        homeViewModel.getCharacters()
        homeViewModel.getCharacters()

        verify(charactersObserver).onChanged(firstPageCharacters)
        verify(charactersObserver).onChanged(firstPageCharacters + secondPageCharacters)
    }

    @Test
    fun `test loading state`() {
        assertFalse(homeViewModel.isLoading.value == true)

        homeViewModel.getCharacters()

        val isLoadingCaptor = ArgumentCaptor.forClass(Boolean::class.java)
        verify(isLoadingObserver, times(3)).onChanged(isLoadingCaptor.capture())

        assertTrue(isLoadingCaptor.allValues[1])
        assertFalse(isLoadingCaptor.allValues[2])
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        homeViewModel.characters.removeObserver(charactersObserver)
        homeViewModel.isLoading.removeObserver(isLoadingObserver)
        homeViewModel.errorConnection.removeObserver(errorConnectionObserver)
        homeViewModel.errorMessage.removeObserver(errorMessageObserver)
    }
}