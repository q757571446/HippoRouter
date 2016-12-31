package com.hippo.router.router;

import java.util.HashMap;
import java.util.Map;

/**
 * fromActivity the help of Request, you can manipulate pager by url
 * the relationship between ActivityRequest and Request is one by one or one by more
 */
public interface IRouter<P extends IRequest> {

    Map<String, Class<?>> ROUTER_TABLE = new HashMap<>();

    boolean canHandle(P request);

    boolean handle(P request);
}
