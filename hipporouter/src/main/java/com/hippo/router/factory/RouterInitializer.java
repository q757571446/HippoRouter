package com.hippo.router.factory;

import java.util.Map;

/**
 * Created by kevin on 16-11-24.
 */

public interface RouterInitializer<T> {
    void initialize(Map<String,Class<? extends T>> tables);
}
