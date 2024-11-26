package com.android.marvel.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.android.marvel.databinding.FragmentCharacterDetailsBinding
import com.android.marvel.ui.api.ApiService
import com.android.marvel.ui.model.Character
import com.android.marvel.ui.model.getFullImageUrl
import com.android.marvel.ui.repo.MarvelRepositoryImp


class CharacterDetailsFragment : Fragment() {

    private lateinit var binding: FragmentCharacterDetailsBinding
    private lateinit var viewModel: DetailViewModel
    private val args: CharacterDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharacterDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupCharacterDetails()
        setupRecyclerViews()
        observeViewModelData()
        setupNavigationListener()
    }

    private fun setupViewModel() {
        val factory = DetailsViewModelFactory(
            MarvelRepositoryImp(ApiService.Api.createApiService())
        )
        viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]
    }

    private fun setupCharacterDetails() {
        val character = args.character
        displayCharacterData(character)
        viewModel.loadCharacter(character)

        // Fetch additional details
        viewModel.apply {
            getStoriesByCharacterId(character.id)
            getEventsByCharacterId(character.id)
            getComicsByCharacterId(character.id)
            getSeriesByCharacterId(character.id)
        }
    }

    private fun setupRecyclerViews() {
        val recyclerViews = listOf(
            binding.comicsRecyclerView to "comics",
            binding.seriesRecyclerView to "series",
            binding.storiesRecyclerView to "stories",
            binding.eventRecyclerView to "event"
        )

        recyclerViews.forEach { (recyclerView, _) ->
            recyclerView.apply {
                adapter = DetailsAdapter(mutableListOf()) {}
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }
        }
    }

    private fun observeViewModelData() {
        val observationMap = mapOf(
            viewModel.comics to (binding.comicsRecyclerView to binding.comicsHeader),
            viewModel.series to (binding.seriesRecyclerView to binding.seriesHeader),
            viewModel.events to (binding.eventRecyclerView to binding.eventHeader),
            viewModel.stories to (binding.storiesRecyclerView to binding.storiesHeader)
        )

        observationMap.forEach { (liveData, viewPair) ->
            liveData.observe(viewLifecycleOwner) { items ->
                (viewPair.first.adapter as DetailsAdapter).insertData(items)

                // Show/hide views based on data
                viewPair.first.isVisible = items.isNotEmpty()
                viewPair.second.isVisible = items.isNotEmpty()
            }
        }
    }

    private fun setupNavigationListener() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun displayCharacterData(character: Character) {
        // Load image
        val imageUrl = character.getFullImageUrl()
        binding.collapsingImage.load(imageUrl)
        binding.blurBackground.load(imageUrl)

        // Set character name and description
        binding.characterName.text = character.name
        binding.characterDescription.text = character.description

        // Handle URL sections
        setupUrlSection(character)

        // Visibility management for name and description
        updateTextVisibility(
            binding.characterName to binding.nameHeader,
            shouldHide = character.name.isBlank()
        )
        updateTextVisibility(
            binding.characterDescription to binding.descriptionHeader,
            shouldHide = character.description.isBlank()
        )
    }

    private fun setupUrlSection(character: Character) {
        val urlTypes = listOf(
            "detail" to binding.detailItem,
            "wiki" to binding.wikiItem,
            "comiclink" to binding.comiclinkItem
        )

        var hasValidUrls = false
        urlTypes.forEach { (type, view) ->
            val url = character.urls?.firstOrNull { it.type == type }?.url
            view.isVisible = !url.isNullOrBlank()

            view.setOnClickListener {
                url?.let { safeUrl ->
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(safeUrl)))
                }
            }

            hasValidUrls = hasValidUrls || !url.isNullOrBlank()
        }

        binding.relatedLinkSection.isVisible = hasValidUrls
    }

    private fun updateTextVisibility(vararg viewPairs: Pair<View, View>, shouldHide: Boolean) {
        viewPairs.forEach { (textView, headerView) ->
            textView.isVisible = !shouldHide
            headerView.isVisible = !shouldHide
        }
    }
}