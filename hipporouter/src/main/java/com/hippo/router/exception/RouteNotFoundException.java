package com.hippo.router.exception;

/**
 * Created by kris on 16/3/10.
 */
public class RouteNotFoundException extends Exception {
    public RouteNotFoundException(String path){
        super(String.format("The route not found: %s", path));
    }
}
