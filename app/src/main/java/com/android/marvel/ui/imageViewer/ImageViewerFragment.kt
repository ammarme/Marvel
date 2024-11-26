package com.android.marvel.ui.imageViewer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.android.marvel.databinding.FragmentViewerBinding


class ViewerFragment : Fragment() {
    private lateinit var binding: FragmentViewerBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewerBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the adapter with comic pages
//        binding.comicViewPager.adapter = ComicPagerAdapter(dummyCharacters)

        // Set up the close button
        binding.closeButton.setOnClickListener {
            findNavController().navigateUp()
        }

        // Update page number when page changes
        binding.comicViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updatePageInfo(position)
//                binding.comicTitleText.text = dummyCharacters[position].name
            }
        })

        // Set initial comic information
        binding.comicTitleText.text = "Ant-Man & The Wasp (2010) #3"
        updatePageInfo(0)
    }

    private fun updatePageInfo(position: Int) {
        binding.pageNumberText.text = "${position + 1} / ${binding.comicViewPager.adapter?.itemCount}"
    }

}