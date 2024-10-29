package com.amazone.bookshop.repository;


import com.amazone.bookshop.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearch {

    private String memberName;
    private OrderStatus orderStatus;
}
