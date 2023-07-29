package com.spro.orderservice.service;

import com.spro.orderservice.dto.OrderLinesItemsDto;
import com.spro.orderservice.dto.OrderRequest;
import com.spro.orderservice.model.Order;
import com.spro.orderservice.model.OrderLineItems;
import com.spro.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.orderLinesItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(orderLineItems);
        orderRepository.save(order);
    }

    private OrderLineItems mapToDto(OrderLinesItemsDto orderLinesItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLinesItemsDto.price());
        orderLineItems.setQuantity(orderLinesItemsDto.quantity());
        orderLineItems.setSkuCode(orderLinesItemsDto.skuCode());
        return orderLineItems;
    }
}
