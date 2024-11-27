package com.android.marvel.app.ui.imageViewer


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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImageViewerFragment : Fragment() {

    private val binding by lazy { FragmentViewerBinding.inflate(layoutInflater) }
    private val args: ImageViewerFragmentArgs by navArgs()

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
        val comicPagerAdapter = ImageViewerPagerAdapter(args.items.toList())

        with(binding.comicViewPager) {
            adapter = comicPagerAdapter

            post { setCurrentItem(args.position, false) }

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
        binding.pageNumberText.text = getString(
            R.string.page_number_format,
            position + 1,
            binding.comicViewPager.adapter?.itemCount ?: 0
        )
    }

    private fun updatePageTitle(position: Int) {
        binding.comicTitleText.text = args.items.getOrNull(position)?.title ?: ""
    }
}