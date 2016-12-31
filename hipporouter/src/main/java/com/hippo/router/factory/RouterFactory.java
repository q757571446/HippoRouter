package com.hippo.router.factory;

import com.hippo.router.router.impl.Request;
import com.hippo.router.router.impl.Router;

import java.util.Map;

/**
 * Created by kevin on 16-11-16.
 */

public interface RouterFactory<T,P extends Request,R extends Router<T,P>>  extends RouterInitializer<T> {
    R buildInstance();
    void initialize(Map<String,Class<? extends T>> tables);
}
