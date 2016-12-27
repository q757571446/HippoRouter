package com.example.library.router.router;

import com.example.library.router.factory.RouterInitializer;
import com.example.library.router.request.impl.Route;

import java.util.HashMap;
import java.util.Map;

/**
 * fromActivity the help of Request, you can manipulate pager by url
 * the relationship between Route and Request is one by one or one by more
 */
public interface IRouter {

    Map<String, Class> ROUTER_TABLE = new HashMap<>();

    void init(RouterInitializer initializer);

    boolean canHandle(String url);

    Map.Entry<String,Class> match(Route route);

    boolean handle(Map.Entry<String,Class> entry);

}
