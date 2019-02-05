package com.zawadzkidevelop.b2borderservice.exception;

public class ShoppingBasketValidationException extends Exception {

    public ShoppingBasketValidationException(String message) {
        super(message);
    }

    public ShoppingBasketValidationException(Exception e) {
        super(e);
    }

}
