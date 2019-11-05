package com.doordash.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.doordash.base.domain.DoorDashManager;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import coil.Coil;
import coil.api.ImageLoaders;
import coil.request.LoadRequest;
import coil.request.RequestDisposable;

/**
 * Manages loading of thumbnails.
 */

@Singleton
public class ImageLoader extends DoorDashManager {

    private static String TAG = ImageLoader.class.getSimpleName();

    private coil.ImageLoader mImageLoader;

    private Map<String, RequestDisposable> mActiveRequests = new HashMap<>();

    @Inject
    public ImageLoader() {
        mImageLoader = Coil.loader();
    }


    public void onLowMemory() {
        mImageLoader.clearMemory();
    }

    public void load(@NonNull Context context, @NonNull final String url, @NonNull final Callback callback) {
        LoadRequest loadRequest = ImageLoaders.newLoadBuilder(mImageLoader, context)
                .data(url)
                .allowHardware(false)
                .lifecycle(ProcessLifecycleOwner.get())
                .bitmapConfig(Bitmap.Config.RGB_565)
                .target(drawable -> {
                    callback.loadingStarted(url);
                    return null;
                }, drawable -> {
                    mActiveRequests.remove(url);
                    callback.loadingError(url);
                    return null;
                }, drawable -> {
                    mActiveRequests.remove(url);
                    callback.loadingCompleted(url, drawableToBitmap(drawable));
                    return null;
                })
                .build();
        try {
            mActiveRequests.put(url, mImageLoader.load(loadRequest));
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public boolean cancelRequest(@NonNull final String url) {
        if (mActiveRequests.containsKey(url)) {
            RequestDisposable requestDisposable = mActiveRequests.get(url);
            if (!requestDisposable.isDisposed()) {
                requestDisposable.dispose();
            }
            mActiveRequests.remove(url);
            return true;
        }
        return false;
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public interface Callback {
        void loadingStarted(String uri);

        void loadingCompleted(String uri, Bitmap bitmap);

        void loadingError(String uri);
    }
}
