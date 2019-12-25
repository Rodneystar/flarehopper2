package com.jdog.redis.flarehopper2;

public class BadRequestException extends  RuntimeException{
    /**
     *
     */
    private static final long serialVersionUID = 5282770306132099983L;

    BadRequestException(String message) {
        super(message);
    }
}
