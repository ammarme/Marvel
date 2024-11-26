package com.android.marvel.ui.imageViewer


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.android.marvel.R
import com.android.marvel.databinding.FragmentViewerBinding

class ImageViewerFragment : Fragment() {
    // Use by lazy for view binding to ensure it's only inflated when needed
    private val binding by lazy { FragmentViewerBinding.inflate(layoutInflater) }

    // Use by navArgs() for safe navigation arguments
    private val args: ImageViewerFragmentArgs by navArgs()

    // Use view lifecycle to manage view-related logic
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupCloseButton()
    }

    private fun setupViewPager() {
        // Create adapter with items from arguments
        val comicPagerAdapter = ComicPagerAdapter(args.items.toList())

        with(binding.comicViewPager) {
            adapter = comicPagerAdapter

            // Set initial position after layout is complete
            post { setCurrentItem(args.position, false) }

            // Set up page change listener
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    updatePageInfo(position)
                    updatePageTitle(position)
                }
            })
        }
    }

    private fun setupCloseButton() {
        binding.closeButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun updatePageInfo(position: Int) {
        // Safe call to adapter and use of position
        binding.pageNumberText.text = getString(
            R.string.page_number_format,
            position + 1,
            binding.comicViewPager.adapter?.itemCount ?: 0
        )
    }

    private fun updatePageTitle(position: Int) {
        // Safely access title with bounds checking
        binding.comicTitleText.text = args.items.getOrNull(position)?.name ?: ""
    }
}