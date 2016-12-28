package com.example.library.router.router;

import com.example.library.router.factory.RouterInitializer;
import com.example.library.router.router.impl.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * fromActivity the help of Request, you can manipulate pager by url
 * the relationship between ActivityRequest and Request is one by one or one by more
 */
public interface IRouter<P extends Request> {

    Map<String, Class<?>> ROUTER_TABLE = new HashMap<>();

    void init(RouterInitializer initializer);

    boolean canHandle(String url);

    boolean handle(P request);
}
