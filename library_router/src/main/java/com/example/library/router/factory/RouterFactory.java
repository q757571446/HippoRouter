package com.example.library.router.factory;

import android.app.Activity;

import com.example.library.router.router.Router;

import java.util.Map;

/**
 * Created by kevin on 16-11-16.
 */

public interface RouterFactory<T extends Router> extends RouterBinder {
    T buildInstance();
}
