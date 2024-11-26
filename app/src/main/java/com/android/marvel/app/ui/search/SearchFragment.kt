package com.android.marvel.app.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.marvel.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
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

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.cancel.setOnClickListener {
            findNavController().navigateUp()
        }

        val adapter = SearchAdapter(mutableListOf())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchByName(newText)
                return true
            }
        })
    }

    private fun observeViewModel() {
        viewModel.characters.observe(viewLifecycleOwner) {
            (binding.recyclerView.adapter as SearchAdapter).setList(it)
        }

        viewModel.errorConnection.observe(viewLifecycleOwner) { hasError ->
            handleErrorConnection(hasError)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            binding.textViewErrorMessage.text = it
        }
    }

    private fun handleErrorConnection(hasError: Boolean) {
        if (hasError) {
            binding.recyclerView.visibility = View.INVISIBLE
            binding.layoutInternetError.visibility = View.VISIBLE
        } else {
            binding.recyclerView.visibility = View.VISIBLE
            binding.layoutInternetError.visibility = View.INVISIBLE
        }
    }
}
