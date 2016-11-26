package com.example.library.router.exception;

/**
 * Created by kris on 16/3/10.
 */
public class InvalidRoutePathException extends Exception{

    public InvalidRoutePathException(String routePath){
        super(String.format("Invalid route path %s", routePath));
    }
}
