package com.android.marvel.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.marvel.R
import com.android.marvel.databinding.FragmentMarvelCharactersBinding
import com.android.marvel.ui.api.ApiService
import com.android.marvel.ui.model.Character
import com.android.marvel.ui.repo.MarvelRepositoryImp
import com.android.marvel.ui.utils.EndlessRecyclerViewScrollListener


class HomeFragment : Fragment() {
    private var _binding: FragmentMarvelCharactersBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using ViewBinding
        _binding = FragmentMarvelCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = HomeViewModelFactory(MarvelRepositoryImp(ApiService.Api.createApiService()))
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        // Set up the toolbar
        setupToolbar()
        addSearchMenu()
        setupCharacterRecyclerView()


        viewModel.listCharacters.observe(viewLifecycleOwner) {
            if (it != null) updateCharacterList(it)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progress.visibility = View.VISIBLE
            } else {
                binding.progress.visibility = View.INVISIBLE
            }
        }

        viewModel.errorConnection.observe(viewLifecycleOwner) { hasError ->
            if (hasError) {
                binding.recyclerView.visibility = View.INVISIBLE
                binding.layoutInternetError.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.layoutInternetError.visibility = View.INVISIBLE
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            binding.textViewErrorMessage.text = it
        }




        binding.textViewTryAgain.setOnClickListener {
            viewModel.getCharacters()
        }

    }

    private fun addSearchMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Inflate the menu here
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                // Handle menu item selection here
                return when (item.itemId) {
                    R.id.action_search -> {
                        findNavController().navigate(R.id.action_marvelCharactersFragment_to_searchFragment)
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner)
    }

    private fun setupToolbar() {
        val toolbar = binding.customToolbar
        (requireActivity() as? AppCompatActivity)?.setSupportActionBar(toolbar)
    }

    private fun setupCharacterRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL , false)

        binding.recyclerView.apply {
            adapter = HomeAdapter(mutableListOf())
            setHasFixedSize(true)
            this.layoutManager = layoutManager
        }

        binding.recyclerView.addOnScrollListener(
            object : EndlessRecyclerViewScrollListener(layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                    viewModel.getCharacters()
                }
            }
        )

    }

    private fun updateCharacterList(charactersList: List<Character>) {
        (binding.recyclerView.adapter as HomeAdapter).addCharacters(charactersList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}