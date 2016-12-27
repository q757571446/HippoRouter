package com.example.library.router.factory;

import com.example.library.router.router.Router;

/**
 * Created by kevin on 16-11-16.
 */

public interface RouterFactory<T extends Router>  extends RouterInitializer {
    T buildInstance();
}
