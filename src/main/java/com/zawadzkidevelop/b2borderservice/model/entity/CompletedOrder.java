package com.zawadzkidevelop.b2borderservice.model.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class CompletedOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basketId", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ShoppingBasket shoppingBasket;

    private Long basketId;

    private String vatNumber;
}
