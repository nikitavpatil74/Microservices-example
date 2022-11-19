package by.rom.customerservice.service;

import by.rom.customerservice.client.NotificationClient;
import by.rom.customerservice.dto.CustomerDto;
import by.rom.customerservice.dto.EmailDto;
import by.rom.customerservice.dto.InventoryResponse;
import by.rom.customerservice.dto.OrderDto;
import by.rom.customerservice.exception.NotFoundException;
import by.rom.customerservice.model.Customer;
import by.rom.customerservice.model.Order;
import by.rom.customerservice.repository.CustomerRepository;
import by.rom.customerservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final NotificationClient notificationClient;
    private final CustomerRepository customerRepository;
    private final WebClient.Builder webClient;

    @Transactional
    public Order createOrder(OrderDto orderDto) {
        Customer customer = findCustomer(orderDto);

        log.info("creating order: {}", orderDto);

        Order order = Order.builder()
                .information(orderDto.getInformation())
                .customer(customer)
                .nameProduct(orderDto.getNameProduct())
                .countOfProduct(orderDto.getCountOfProduct())
//                .customerId(orderDto.getCustomerId())
                .build();

        boolean hasProduct = findProduct(order.getNameProduct());

        log.info("saving order to the DB: {}", orderDto);

        Order savedOrder;

        if (!hasProduct){
            savedOrder  = orderRepository.save(order);
            log.info("saved order {}", savedOrder);
        }
        else {
            throw new IllegalArgumentException("Product isn't in stock, please try again later");
        }

        sendEmail(customer);

        return savedOrder;
    }

    public List<Order> getOrders(CustomerDto customerDto) {
        List<Order> list = orderRepository.findOrderByCustomerNumberAndEmail(customerDto.getPhoneNumber());
        if (list.isEmpty()){
            throw new NotFoundException("Customer doesn't have any orders, try again.");
        }
        else
            return list;
    }

    private boolean findProduct(String nameProduct) {

        InventoryResponse[] result = webClient.build().get()
                .uri("http://inventory-service/api/product/inventory",
                        uriBuilder -> uriBuilder.queryParam("nameProduct", nameProduct).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        log.info("result from inventory service: {}", result);

        return Arrays.stream(Objects.requireNonNull(result))
                .allMatch(InventoryResponse::isInStock);
    }

    private Customer findCustomer(OrderDto orderDto) {
        return customerRepository.findById(orderDto.getCustomerId())
                .orElseThrow(() -> new NotFoundException("customer not found with id: " + orderDto.getCustomerId()));
    }

    private void sendEmail(Customer customer) {
        EmailDto emailDto = EmailDto.builder()
                .email(customer.getEmail())
                .body(String.format("Thanks %s for your order.", customer.getName()))
                .build();
        log.info("sending email {}" , emailDto);
        notificationClient.sendEmail(emailDto);
    }


}
