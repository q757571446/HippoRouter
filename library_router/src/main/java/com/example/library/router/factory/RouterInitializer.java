package com.example.library.router.factory;

import java.util.Map;

/**
 * Created by kevin on 16-11-24.
 */

public interface RouterInitializer {
    void put(Map<String,Class> tables);
}
