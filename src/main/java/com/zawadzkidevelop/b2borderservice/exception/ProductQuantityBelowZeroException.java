package com.zawadzkidevelop.b2borderservice.exception;

public class ProductQuantityBelowZeroException extends Exception {

    public ProductQuantityBelowZeroException(String message) {
        super(message);
    }

    public ProductQuantityBelowZeroException(Exception e) {
        super(e);
    }

}
