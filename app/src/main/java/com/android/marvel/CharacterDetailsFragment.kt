package com.android.marvel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.android.marvel.databinding.FragmentCharacterDetailsBinding


class CharacterDetailsFragment : Fragment() {

    private lateinit var binding: FragmentCharacterDetailsBinding
    private val args: CharacterDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharacterDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val character = args.character
        binding.collapsingImage.load(character.imageUrl)
        binding.blurBackground.load(character.imageUrl)

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.comicsRecyclerView.apply {
            adapter = ItemDetailsAdapter(dummyCharacters) {
                findNavController().navigate(R.id.action_characterDetailsFragment_to_viewerFragment)
            }
            layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.HORIZONTAL , false)
        }

        binding.seriesRecyclerView.apply {
            adapter = ItemDetailsAdapter(dummyCharacters) {
                findNavController().navigate(R.id.action_characterDetailsFragment_to_viewerFragment)
            }
            layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.HORIZONTAL , false)
        }

        binding.storiesRecyclerView.apply {
            adapter = ItemDetailsAdapter(dummyCharacters) {
                findNavController().navigate(R.id.action_characterDetailsFragment_to_viewerFragment)
            }
            layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.HORIZONTAL , false)
        }

        binding.eventRecyclerView.apply {
            adapter = ItemDetailsAdapter(dummyCharacters) {
                findNavController().navigate(R.id.action_characterDetailsFragment_to_viewerFragment)
            }
            layoutManager = LinearLayoutManager(requireContext() , LinearLayoutManager.HORIZONTAL , false)
        }

    }
}