package com.android.marvel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.marvel.app.model.Character
import com.android.marvel.app.model.DetailItem
import com.android.marvel.app.model.DetailsData
import com.android.marvel.app.model.DetailsResponse
import com.android.marvel.app.model.Thumbnail
import com.android.marvel.app.repo.MarvelRepository
import com.android.marvel.app.ui.details.DetailViewModel
import com.android.marvel.app.ui.details.DetailsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Mock
    lateinit var marvelRepository: MarvelRepository

    private lateinit var viewModel: DetailViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.openMocks(this)
        viewModel = DetailViewModel(marvelRepository)
    }

    @Test
    fun `test getComicsByCharacterId should post comics list to LiveData`() = runTest {
        val characterId = "123"
        val comicsResponse = DetailsResponse(
            data = DetailsData(
                results = listOf(
                    DetailItem(id = "1", title = "Comic 1", thumbnail = Thumbnail("http://image.com"))
                )
            )
        )

        `when`(marvelRepository.getComicsByCharacterId(characterId)).thenReturn(comicsResponse)

        viewModel.getComicsByCharacterId(characterId)

        val comics = viewModel.comics.value
        assertNotNull(comics)
        assertEquals(1, comics?.size)
        assertEquals("Comic 1", comics?.get(0)?.title)
    }

    @Test(expected = Exception::class)
    fun `test getComicsByCharacterId should handle error`() = runTest {
        val characterId = "123"

        `when`(marvelRepository.getComicsByCharacterId(characterId)).thenThrow(Exception("Network Error"))

        viewModel.getComicsByCharacterId(characterId)

        val comics = viewModel.comics.value
        assertTrue(comics.isNullOrEmpty())
    }

    @Test
    fun `test updateCharacter should update character state`() = runTest{
        val character = Character(id = "1", name = "Iron Man", description = "Test Description")

        val seriesResponse = DetailsResponse(
            data = DetailsData(
                results = listOf(
                    DetailItem(id = "1", title = "Series 1", thumbnail = Thumbnail("http://image.com"))
                )
            )
        )
        val eventsResponse = DetailsResponse(
            data = DetailsData(
                results = listOf(
                    DetailItem(id = "1", title = "Event 1", thumbnail = Thumbnail("http://image.com"))
                )
            )
        )
        val storiesResponse = DetailsResponse(
            data = DetailsData(
                results = listOf(
                    DetailItem(id = "1", title = "Story 1", thumbnail = Thumbnail("http://image.com"))
                )
            )
        )
        val comicsResponse = DetailsResponse(
            data = DetailsData(
                results = listOf(
                    DetailItem(id = "1", title = "Comic 1", thumbnail = Thumbnail("http://image.com"))
                )
            )
        )
        `when`(marvelRepository.getStoriesByCharacterId(character.id)).thenReturn(storiesResponse)
        `when`(marvelRepository.getComicsByCharacterId(character.id)).thenReturn(comicsResponse)
        `when`(marvelRepository.getEventByCharacterId(character.id)).thenReturn(eventsResponse)
        `when`(marvelRepository.getSeriesByCharacterId(character.id)).thenReturn(seriesResponse)

        viewModel.updateCharacter(character)

        val characterState = viewModel.characterState.value
        assertTrue(characterState is DetailsState.Success)

        val successState = characterState as DetailsState.Success
        assertEquals("Iron Man", successState.character.name)
    }

    @Test
    fun `test getSeriesByCharacterId should post series list to LiveData`() = runTest {
        val characterId = "123"
        val seriesResponse = DetailsResponse(
            data = DetailsData(
                results = listOf(
                    DetailItem(id = "1", title = "Series 1", thumbnail = Thumbnail("http://image.com"))
                )
            )
        )

        `when`(marvelRepository.getSeriesByCharacterId(characterId)).thenReturn(seriesResponse)

        viewModel.getSeriesByCharacterId(characterId)

        val series = viewModel.series.value
        assertNotNull(series)
        assertEquals(1, series?.size)
        assertEquals("Series 1", series?.get(0)?.title)
        assertEquals("http://image.com", series?.get(0)?.thumbnail?.path)
    }

    @Test(expected = Exception::class)
    fun `test getSeriesByCharacterId should handle error`() = runTest {
        val characterId = "123"

        `when`(marvelRepository.getSeriesByCharacterId(characterId)).thenThrow(Exception("Network Error"))

        viewModel.getSeriesByCharacterId(characterId)

        val series = viewModel.series.value
        assertTrue(series.isNullOrEmpty())
    }

    @Test
    fun `test getEventsByCharacterId should post events list to LiveData`() = runTest {
        val characterId = "123"
        val eventsResponse = DetailsResponse(
            data = DetailsData(
                results = listOf(
                    DetailItem(id = "1", title = "Event 1", thumbnail = Thumbnail("http://image.com"))
                )
            )
        )

        `when`(marvelRepository.getEventByCharacterId(characterId)).thenReturn(eventsResponse)

        viewModel.getEventsByCharacterId(characterId)

        val events = viewModel.events.value
        assertNotNull(events)
        assertEquals(1, events?.size)
        assertEquals("Event 1", events?.get(0)?.title)
        assertEquals("http://image.com", events?.get(0)?.thumbnail?.path)
    }

    @Test(expected = Exception::class)
    fun `test getEventsByCharacterId should handle error`() = runTest {
        val characterId = "123"

        `when`(marvelRepository.getEventByCharacterId(characterId)).thenThrow(Exception("Network Error"))

        viewModel.getEventsByCharacterId(characterId)

        val events = viewModel.events.value
        assertTrue(events.isNullOrEmpty())
    }

    @Test
    fun `test getStoriesByCharacterId should post stories list to LiveData`() = runTest {
        val characterId = "123"
        val storiesResponse = DetailsResponse(
            data = DetailsData(
                results = listOf(
                    DetailItem(id = "1", title = "Story 1", thumbnail = Thumbnail("http://image.com"))
                )
            )
        )

        `when`(marvelRepository.getStoriesByCharacterId(characterId)).thenReturn(storiesResponse)

        viewModel.getStoriesByCharacterId(characterId)

        val stories = viewModel.stories.value
        assertNotNull(stories)
        assertEquals(1, stories?.size)
        assertEquals("Story 1", stories?.get(0)?.title)
        assertEquals("http://image.com", stories?.get(0)?.thumbnail?.path)
    }

    @Test(expected = Exception::class)
    fun `test getStoriesByCharacterId should handle error`() = runTest {
        val characterId = "123"

        `when`(marvelRepository.getStoriesByCharacterId(characterId)).thenThrow(Exception("Network Error"))

        viewModel.getStoriesByCharacterId(characterId)

        val stories = viewModel.stories.value
        assertTrue(stories.isNullOrEmpty())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}