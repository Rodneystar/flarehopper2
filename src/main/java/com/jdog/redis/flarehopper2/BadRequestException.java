package com.jdog.redis.flarehopper2;

public class BadRequestException extends  RuntimeException{
    BadRequestException(String message) {
        super(message);
    }
}
