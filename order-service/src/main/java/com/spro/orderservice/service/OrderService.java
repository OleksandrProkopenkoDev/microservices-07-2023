package com.spro.orderservice.service;

import com.spro.orderservice.dto.InventoryResponse;
import com.spro.orderservice.dto.OrderLinesItemsDto;
import com.spro.orderservice.dto.OrderRequest;
import com.spro.orderservice.model.Order;
import com.spro.orderservice.model.OrderLineItems;
import com.spro.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.orderLinesItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();
        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

//        call Inventory Service, and place order if product is in stock
        InventoryResponse[] inventoryResponseArray = webClient.get()
                .uri(
                        "http://localhost:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build()
                        )
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        assert inventoryResponseArray != null;
        Arrays.stream(inventoryResponseArray).forEach(System.out::println);
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                .allMatch(InventoryResponse::isInInStock);
        if(allProductsInStock) {
            orderRepository.save(order);
        }else {
            throw new IllegalArgumentException("Products are not in stock");
        }
    }

    private OrderLineItems mapToDto(OrderLinesItemsDto orderLinesItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLinesItemsDto.price());
        orderLineItems.setQuantity(orderLinesItemsDto.quantity());
        orderLineItems.setSkuCode(orderLinesItemsDto.skuCode());
        return orderLineItems;
    }
}
