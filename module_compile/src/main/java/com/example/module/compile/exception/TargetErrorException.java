package com.example.module.compile.exception;

/**
 * Created by kris on 16/4/20.
 */
public class TargetErrorException extends RuntimeException {

    public TargetErrorException(){
        super("Annotated target error, it should annotate only class");
    }
}
