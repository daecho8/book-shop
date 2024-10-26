package com.amazone.bookshop.service;

import com.amazone.bookshop.domain.Address;
import com.amazone.bookshop.domain.Member;
import com.amazone.bookshop.domain.Order;
import com.amazone.bookshop.domain.OrderStatus;
import com.amazone.bookshop.domain.item.Book;
import com.amazone.bookshop.domain.item.Item;
import com.amazone.bookshop.exception.NotEnoughStockException;
import com.amazone.bookshop.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    void 상품주문() {
        //given
        Member member = createMember();
        Book book = createBook();
        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus());
        assertEquals(1, getOrder.getOrderItems().size());
        assertEquals(10000 * orderCount, getOrder.getTotalPrice());
        assertEquals(8, book.getStockQuantity());
    }

    @Test
    void 상품주문_재고수량초과() {
        //given
        Member member = createMember();
        Item item = createBook();
        int orderCount = 11;

        //when

        //then
        Assertions.assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), item.getId(), orderCount);
        });
    }

    @Test
    void 주문취소() {
        //given
        Member member = createMember();
        Book item = createBook();
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        Assertions.assertEquals(OrderStatus.CANCEL, getOrder.getStatus());
        Assertions.assertEquals(10, item.getStockQuantity());
    }

    private Book createBook() {
        Book book = new Book();
        book.setName("cho");
        book.setPrice(10000);
        book.setStockQuantity(10);
        em.persist(book);

        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("user1");
        member.setAddress(new Address("seoul", "street", "1234"));
        em.persist(member);

        return member;
    }
}