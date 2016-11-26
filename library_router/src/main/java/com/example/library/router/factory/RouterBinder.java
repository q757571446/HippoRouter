package com.example.library.router.factory;

import java.util.Map;

/**
 * Created by kevin on 16-11-24.
 */

public interface RouterBinder {
    Map<String, Class> getRouterTable();
}
