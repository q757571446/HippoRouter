package com.example.library.router.router;



import android.app.Activity;
import android.content.Context;

import com.example.library.router.request.impl.RouterRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * fromActivity the help of Request, you can manipulate pager by url
 * the relationship between RouterRequest and Request is one by one or one by more
 */
public interface IRouter {

    Map<String, Class> ROUTER_TABLE = new HashMap<>();

    boolean open(RouterRequest request);

    /**
     * decide if the url can be opened
     * @param url
     * @return
     */
    boolean canOpenTheUrl(String url);


    Map<String,Class> getRouterTableFromMemory();
}
