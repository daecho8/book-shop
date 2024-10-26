package com.amazone.bookshop.service;

import com.amazone.bookshop.domain.Delivery;
import com.amazone.bookshop.domain.Member;
import com.amazone.bookshop.domain.Order;
import com.amazone.bookshop.domain.OrderItem;
import com.amazone.bookshop.domain.item.Item;
import com.amazone.bookshop.repository.ItemRepository;
import com.amazone.bookshop.repository.MemberRepository;
import com.amazone.bookshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        Order order = Order.createOrder(member, delivery, orderItem);
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);
        order.cancel();
    }

}
