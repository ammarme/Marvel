package com.android.marvel.app.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.marvel.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val searchAdapter = SearchAdapter(mutableListOf())
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearch()
        observeViewModel()
        binding.cancel.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.updateSearchQuery(newText?.trim().orEmpty())
                return true
            }
        })
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchState.collect { state ->
                when (state) {
                    is SearchState.Idle -> {
                        binding.progressBar.isVisible = false
                    }

                    is SearchState.Loading -> {
                        binding.progressBar.isVisible = true
                    }

                    is SearchState.Success -> {
                        binding.progressBar.isVisible = false
                        searchAdapter.setList(state.characters)
                    }

                    is SearchState.Error -> {
                        binding.progressBar.isVisible = false
                        Toast.makeText(requireContext(),state.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

}
