package com.zawadzkidevelop.b2borderservice.service;

import com.zawadzkidevelop.b2borderservice.exception.ProductQuantityBelowZeroException;
import com.zawadzkidevelop.b2borderservice.exception.ShoppingBasketValidationException;
import com.zawadzkidevelop.b2borderservice.model.dto.ShoppingBasketDTO;
import com.zawadzkidevelop.b2borderservice.model.entity.*;
import com.zawadzkidevelop.b2borderservice.model.repository.CompletedOrderRepository;
import com.zawadzkidevelop.b2borderservice.model.repository.ProductOrderRepository;
import com.zawadzkidevelop.b2borderservice.model.repository.ProductRepository;
import com.zawadzkidevelop.b2borderservice.model.repository.ShoppingBasketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Slf4j
public class ShoppingBasketService {

    private final ShoppingBasketRepository shoppingBasketRepository;

    private final ProductOrderRepository productOrderRepository;

    private final ProductRepository productRepository;

    private final VatNumberValidationService vatNumberValidationService;

    private final CompletedOrderRepository completedOrderRepository;

    private final MessageSource messageSource;

    private final Locale locale = LocaleContextHolder.getLocale();

    @Autowired
    public ShoppingBasketService(ShoppingBasketRepository shoppingBasketRepository, ProductOrderRepository productOrderRepository,
                                 ProductRepository productRepository, VatNumberValidationService vatNumberValidationService,
                                 CompletedOrderRepository completedOrderRepository, MessageSource messageSource) {
        this.shoppingBasketRepository = shoppingBasketRepository;
        this.productOrderRepository = productOrderRepository;
        this.productRepository = productRepository;
        this.vatNumberValidationService = vatNumberValidationService;
        this.completedOrderRepository = completedOrderRepository;
        this.messageSource = messageSource;
    }


    public Long createNewShoppingBasket(String userName) {
        ShoppingBasket shoppingBasket = new ShoppingBasket();
        shoppingBasket.setUserName(userName);
        shoppingBasket.setBasketStatus(BasketStatus.OPEN);
        return shoppingBasketRepository.save(shoppingBasket).getId();
    }

    public List<ShoppingBasketDTO> getShoppingBasketSummary(Long basketId) {
        return productOrderRepository.getShoppingBasketSummary(basketId);
    }

    @Transactional
    public void addProductToBasket(Long basketId, String productId, Long quantity) throws ShoppingBasketValidationException, ProductQuantityBelowZeroException {
        Optional<ShoppingBasket> shoppingBasket = shoppingBasketRepository.findById(basketId);
        if (shoppingBasket.isPresent()) {
            saveOrderIntoBasket(basketId, productId, quantity, shoppingBasket.get());
        } else {
            throw new ShoppingBasketValidationException(messageSource.getMessage("basket.notFound", null, locale));
        }
    }

    private void saveOrderIntoBasket(Long basketId, String productId, Long quantity, ShoppingBasket shoppingBasket) throws ProductQuantityBelowZeroException, ShoppingBasketValidationException {
        if (!isBasketCompleted(shoppingBasket)) {
            Optional<Product> product = productRepository.findById(productId);
            if (!product.isPresent()) {
                saveNewProduct(new Product(productId));
            }
            saveProductIntoBasket(basketId, productId, quantity);
        } else {
            throw new ShoppingBasketValidationException(messageSource.getMessage("basket.completed", null, locale));
        }
    }

    private void saveNewProduct(Product product) {
        productRepository.save(product);
        log.info("New product: {} - saved in database", product.getProductId());
    }

    private void saveProductIntoBasket(Long basketId, String productId, Long quantity) throws ProductQuantityBelowZeroException {
        if (quantity == null || quantity == 0) {
            productOrderRepository.deleteProductOrderByShoppingBasketIdAndProductId(basketId, productId);
        } else if (quantity > 0) {
            productOrderRepository.save(getProductOrderToSave(basketId, productId, quantity));
        } else {
            throw new ProductQuantityBelowZeroException(messageSource.getMessage("productOrder.quantity.belowZero", null, locale));
        }
    }

    private ProductOrder getProductOrderToSave(Long basketId, String productId, Long quantity) {
        ProductOrder productOrder = productOrderRepository.findProductOrderByBasketIdAndProductId(basketId, productId);
        if (productOrder == null) {
            productOrder = new ProductOrder();
        }
        productOrder.setBasketId(basketId);
        productOrder.setProductId(productId);
        productOrder.setQuantity(quantity);
        return productOrder;
    }

    public void deleteShoppingBasket(Long basketId) throws ShoppingBasketValidationException {
        Optional<ShoppingBasket> shoppingBasket = shoppingBasketRepository.findById(basketId);
        if (shoppingBasket.isPresent() && !isBasketCompleted(shoppingBasket.get())) {
            deleteBasket(shoppingBasket.get());
        } else {
            throw new ShoppingBasketValidationException(messageSource.getMessage("basket.completed", null, locale));
        }
    }

    private void deleteBasket(ShoppingBasket shoppingBasket) throws ShoppingBasketValidationException {
        if (!isBasketCompleted(shoppingBasket)) {
            productOrderRepository.deleteProductOrderByBasketId(shoppingBasket.getId());
            shoppingBasketRepository.deleteById(shoppingBasket.getId());
            log.info("Shopping basket with id: {} - deleted from database", shoppingBasket.getId());
        } else {
            throw new ShoppingBasketValidationException(messageSource.getMessage("basket.completed", null, locale));
        }
    }

    private boolean isBasketCompleted(ShoppingBasket shoppingBasket) {
        return shoppingBasket.getBasketStatus().equals(BasketStatus.COMPLETED);
    }

    @Transactional
    public Optional<Long> completeOrder(Long basketId, String vatNumber) throws ShoppingBasketValidationException {
        Optional<ShoppingBasket> shoppingBasket = shoppingBasketRepository.findById(basketId);
        if (shoppingBasket.isPresent() && shoppingBasket.get().getBasketStatus().equals(BasketStatus.OPEN)
                && vatNumberValidationService.validateVatNumber(vatNumber)) {
            updateBasketStatus(basketId, shoppingBasket.get());
            CompletedOrder completedOrder = new CompletedOrder();
            completedOrder.setBasketId(basketId);
            completedOrder.setVatNumber(vatNumber);
            return Optional.of(completedOrderRepository.save(completedOrder).getId());
        } else {
            throw new ShoppingBasketValidationException(messageSource.getMessage("basket.notFound", null, locale));
        }
    }

    private void updateBasketStatus(Long basketId, ShoppingBasket shoppingBasket) {
        shoppingBasket.setBasketStatus(BasketStatus.COMPLETED);
        shoppingBasketRepository.save(shoppingBasket);
        log.info("Shopping basket with id: {} - ORDER COMPLETED", basketId);
    }
}
