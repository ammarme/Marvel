package com.android.marvel.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.marvel.R
import com.android.marvel.app.utils.EndlessRecyclerViewScrollListener
import com.android.marvel.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var homeAdapter: HomeAdapter
    private var scrollListener: EndlessRecyclerViewScrollListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        addSearchMenu()
        setupCharacterRecyclerView()
        observeViewModel()
    }

    private fun addSearchMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_main, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
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
        val layoutManager = LinearLayoutManager(requireContext())

        homeAdapter = HomeAdapter(mutableListOf())
        binding.recyclerView.apply {
            adapter = homeAdapter
            this.layoutManager = layoutManager
        }

        scrollListener =  object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                viewModel.getCharacters()
            }
        }

        binding.recyclerView.addOnScrollListener(scrollListener!!)

        binding.textViewTryAgain.setOnClickListener {
            scrollListener?.resetFailure()
            viewModel.getCharacters()
        }
    }

    private fun observeViewModel() {
        viewModel.characters.observe(viewLifecycleOwner) { characterList ->
            characterList?.let { homeAdapter.updateCharacters(it) }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progress.isVisible = isLoading
        }

        viewModel.errorConnection.observe(viewLifecycleOwner) { hasError ->
            updateErrorState(hasError)
            scrollListener?.resetFailure()
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrBlank() && homeAdapter.itemCount > 0) {
                showToast(errorMessage)
            } else {
                binding.textViewErrorMessage.text = errorMessage
            }
        }
    }

    private fun updateErrorState(hasError: Boolean) {
        binding.recyclerView.isVisible = !hasError || homeAdapter.itemCount > 0
        binding.layoutInternetError.isVisible = hasError && homeAdapter.itemCount == 0
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
