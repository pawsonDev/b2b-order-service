package com.zawadzkidevelop.b2borderservice.model.repository;

import com.zawadzkidevelop.b2borderservice.model.dto.ShoppingBasketDTO;
import com.zawadzkidevelop.b2borderservice.model.entity.ProductOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrder, Long> {

    @Query(value = "select p.productId as productId, po.quantity as quantity\n" +
            "from ProductOrder po \n" +
            "join po.shoppingBasket sb \n" +
            "join po.product p \n" +
            "where sb.id = :basketId")
    List<ShoppingBasketDTO> getShoppingBasketSummary(@Param("basketId") Long basketId);

    @Modifying
    @Transactional
    void deleteProductOrderByShoppingBasketIdAndProductId(Long basketId, String productId);

    ProductOrder findProductOrderByBasketIdAndProductId(Long basketId, String productId);

    @Modifying
    @Transactional
    void deleteProductOrderByBasketId(Long basketId);
}
