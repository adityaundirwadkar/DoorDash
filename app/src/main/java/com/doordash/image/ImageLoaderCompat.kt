package com.doordash.image

import android.graphics.drawable.Drawable
import coil.ImageLoader
import coil.request.GetRequest
import kotlinx.coroutines.runBlocking

object ImageLoaderCompat {
    @JvmStatic
    fun getBlocking(
            imageLoader: ImageLoader,
            request: GetRequest
    ): Drawable = runBlocking { imageLoader.get(request) }
}