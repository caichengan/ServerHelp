package com.xht.android.serverhelp.util;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.xht.android.serverhelp.App;

/**
 * Created by Administrator on 2016/10/22.
 */

public class MySingletonNetworkIv {
    private static MySingletonNetworkIv mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    //private static Context mCtx;

    private MySingletonNetworkIv() {
        //mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized MySingletonNetworkIv getInstance() {
        if (mInstance == null) {
            mInstance = new MySingletonNetworkIv();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            //mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
            mRequestQueue = App.getInstance().getRequestQueue();
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
