package com.zawadzkidevelop.b2borderservice.controller;

import com.zawadzkidevelop.b2borderservice.exception.ProductQuantityBelowZeroException;
import com.zawadzkidevelop.b2borderservice.exception.ShoppingBasketValidationException;
import com.zawadzkidevelop.b2borderservice.model.dto.ShoppingBasketDTO;
import com.zawadzkidevelop.b2borderservice.service.ShoppingBasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/basket")
public class ShoppingBasketController {

    private final ShoppingBasketService shoppingBasketService;

    @Autowired
    public ShoppingBasketController(ShoppingBasketService shoppingBasketService) {
        this.shoppingBasketService = shoppingBasketService;
    }

    @PostMapping(value = "/{userName}")
    public ResponseEntity createNewShoppingBasket(@PathVariable("userName") String userName) {
        Long basketId = shoppingBasketService.createNewShoppingBasket(userName);
        if (basketId != null) {
            return ResponseEntity.ok(basketId);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/{basketId}")
    public ResponseEntity getShoppingBasketSummary(@PathVariable("basketId") Long basketId) {
        List<ShoppingBasketDTO> basketSummary = shoppingBasketService.getShoppingBasketSummary(basketId);
        return ResponseEntity.ok(basketSummary);
    }

    @PutMapping(value = "/{basketId}/add")
    public ResponseEntity addProductToBasket(@PathVariable("basketId") Long basketId, @PathParam("productId") String productId,
                                             @PathParam("quantity") Long quantity) throws ProductQuantityBelowZeroException, ShoppingBasketValidationException {
        shoppingBasketService.addProductToBasket(basketId, productId, quantity);
        return getShoppingBasketSummary(basketId);
    }

    @DeleteMapping(value = "/{basketId}")
    public void deleteShoppingBasket(@PathVariable("basketId") Long basketId) throws ShoppingBasketValidationException {
        shoppingBasketService.deleteShoppingBasket(basketId);
    }

    @PutMapping(value = "/{basketId}/complete")
    public ResponseEntity completeOrder(@PathVariable("basketId") Long basketId, @PathParam("vatNumber") String vatNumber) throws ShoppingBasketValidationException {
        Optional<Long> completedOrderId = shoppingBasketService.completeOrder(basketId, vatNumber);
        if (completedOrderId.isPresent()) {
            return getShoppingBasketSummary(basketId);
        }
        return ResponseEntity.badRequest().build();
    }


}
