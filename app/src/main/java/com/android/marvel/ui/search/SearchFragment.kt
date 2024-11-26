package com.android.marvel.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.marvel.databinding.FragmentSearchBinding
import com.android.marvel.ui.api.ApiService
import com.android.marvel.ui.repo.MarvelRepositoryImp


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding

    private lateinit var viewModel: SearchViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = SearchViewModelFactory(MarvelRepositoryImp(ApiService.Api.createApiService()))
        viewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]

        binding.cancel.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SearchAdapter(mutableListOf())
        }

        viewModel.listCharacters.observe(viewLifecycleOwner) {
            (binding.recyclerView.adapter as SearchAdapter).setList(it)
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

        // Set up search query listener
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun filterList(query: String?) {
        viewModel.searchByName(query)
    }
}
