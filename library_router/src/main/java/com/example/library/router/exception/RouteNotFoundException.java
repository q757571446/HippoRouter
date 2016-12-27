package com.example.library.router.exception;

import com.example.library.router.request.impl.Route;

/**
 * Created by kris on 16/3/10.
 */
public class RouteNotFoundException extends RuntimeException {
    public RouteNotFoundException(Route route){
        super(String.format("The route not found: %s", route.getUrl()));
    }
}
