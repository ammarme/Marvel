package com.android.marvel.ui.utils

import android.util.Log
import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil.load
import coil.request.ImageRequest
import com.android.marvel.BuildConfig
import com.android.marvel.R
import java.util.concurrent.TimeUnit

/**
 * Load an image into an ImageView with custom headers using Coil.
 *
 * @param url The URL of the image.
 * @param headers A map of custom headers for the request. Duplicate keys will be ignored.
 * @param placeholder Placeholder resource ID while the image loads.
 * @param error Error resource ID in case the image fails to load.
 */
fun ImageView.loadImageWithHeaders(
    url: String,
    @DrawableRes placeholder: Int = R.drawable.ic_launcher_foreground,
    @DrawableRes error: Int = R.drawable.ic_launcher_background
) {
    val timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis())

    // Prepare headers with required values
    val headers = mapOf(
        "apikey" to BuildConfig.API_PUBLIC,
        "hash" to HashGenerate.generate(
            timeStamp,
            BuildConfig.API_PRIVATE,
            BuildConfig.API_PUBLIC
        ),
        "ts" to timeStamp.toString()
    )

    val request = ImageRequest.Builder(context)
        .data(url)
        .placeholder(placeholder)
        .error(error)
        .apply {
            headers.forEach { (key, value) ->
                addHeader(key, value)
            }
        }
        .listener(
            onStart = {
                Log.d("CoilImageLoader", "Request started for URL: $url")
            },
            onSuccess = { _, _ ->
                Log.d("CoilImageLoader", "Image loaded successfully for URL: $url")
            },
            onError = { _, throwable ->
                Log.e("CoilImageLoader", "Failed to load image for URL: $url", throwable.throwable)
            }
        )
        .build()

    this.load(request)
}
