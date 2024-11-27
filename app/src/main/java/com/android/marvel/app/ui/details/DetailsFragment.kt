package com.android.marvel.app.ui.details

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.marvel.R
import com.android.marvel.app.model.Character
import com.android.marvel.app.model.DetailItem
import com.android.marvel.app.model.getFullImageUrl
import com.android.marvel.databinding.FragmentDetailsBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var binding: FragmentDetailsBinding
    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCharacterDetails()
        setupRecyclerViews()
        observeViewModelData()
        setupNavigationListener()
    }

    private fun setupCharacterDetails() {
        val character = args.character
        viewModel.updateCharacter(character)

        viewModel.characterState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is DetailsState.Idle -> {
                    // Optionally handle idle state, maybe show initial loading
                }
                is DetailsState.Loading -> {
                    // Show loading indicator if needed
                }
                is DetailsState.Success -> {
                    displayCharacterData(state.character)
                }
                is DetailsState.Error -> {
                    // Handle error state
                    showErrorState(state.message)
                }
            }
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.apply {
            adapter = DetailsAdapter(mutableListOf())
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    private fun setupRecyclerViews() {
        listOf(
            binding.comicsRecyclerView,
            binding.seriesRecyclerView,
            binding.storiesRecyclerView,
            binding.eventRecyclerView
        ).forEach { recyclerView ->
            setupRecyclerView(recyclerView)
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
                Log.d("TAG", "observeViewModelData: $items")
                (viewPair.first.adapter as DetailsAdapter).updateData(items)
                updateRecyclerViewVisibility(viewPair.first, viewPair.second, items)
            }
        }
    }

    private fun setupNavigationListener() {
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showErrorState(errorMessage: String) {
        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_LONG).show()
    }

    private fun displayCharacterData(character: Character) {
        val imageUrl = character.thumbnail?.getFullImageUrl()
        binding.collapsingImage.load(imageUrl) {
            placeholder(R.color.black)
            error(R.color.black)
        }
        binding.blurBackground.load(imageUrl) {
            placeholder(R.color.black)
            error(R.color.black)
        }

        binding.characterName.text = character.name
        binding.characterDescription.text = character.description

        setupUrlSection(character)

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
            setupUrl(view, url)
            hasValidUrls = hasValidUrls || !url.isNullOrBlank()
        }

        binding.relatedLinkSection.isVisible = hasValidUrls
    }

    private fun setupUrl(view: View, url: String?) {
        view.isVisible = !url.isNullOrBlank()
        view.setOnClickListener {
            url?.let { safeUrl -> launchCustomTab(safeUrl) }
        }
    }

    private fun launchCustomTab(url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(url))
    }

    private fun updateRecyclerViewVisibility(
        recyclerView: RecyclerView,
        headerView: View,
        items: List<DetailItem>
    ) {
        recyclerView.isVisible = items.isNotEmpty()
        headerView.isVisible = items.isNotEmpty()
    }

    private fun updateTextVisibility(vararg viewPairs: Pair<View, View>, shouldHide: Boolean) {
        viewPairs.forEach { (textView, headerView) ->
            textView.isVisible = !shouldHide
            headerView.isVisible = !shouldHide
        }
    }
}