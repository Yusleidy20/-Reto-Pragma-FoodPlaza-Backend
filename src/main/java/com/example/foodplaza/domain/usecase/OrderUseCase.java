package com.example.foodplaza.domain.usecase;


import com.example.foodplaza.application.dto.request.TraceabilityRequestDto;
import com.example.foodplaza.domain.api.IOrderServicePort;
import com.example.foodplaza.domain.api.IValidatorServicePort;
import com.example.foodplaza.domain.model.OrderModel;
import com.example.foodplaza.domain.spi.feignclients.ISmsClientPort;
import com.example.foodplaza.domain.spi.persistence.IOrderDishPersistencePort;
import com.example.foodplaza.domain.spi.persistence.IOrderPersistencePort;
import com.example.foodplaza.infrastructure.configuration.Constants;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.UserDto;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper.ITraceabilityFeignClient;
import com.example.foodplaza.infrastructure.out.jpa.feignclients.mapper.IUserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
public class OrderUseCase implements IOrderServicePort {

    private final IOrderPersistencePort orderPersistencePort;
    private final IOrderDishPersistencePort orderDishPersistencePort;
    private final IValidatorServicePort validatorServicePort;
    private final IUserFeignClient userClientPort;
    private final ISmsClientPort smsClientPort;
    private final ITraceabilityFeignClient traceabilityFeignClient;
    private static final Random RANDOM = new Random();
    @Override
    public OrderModel createOrder(OrderModel orderModel) {
        validateCreateOrder(orderModel);

        Long customerId = getAuthenticatedUserId();
        orderModel.setCustomerId(customerId);
        orderModel.setStateOrder(Constants.PENDING);
        orderModel.setDateOrder(LocalDate.now());

        OrderModel createdOrder = orderPersistencePort.saveOrder(orderModel);

        logTraceability(createdOrder, null, Constants.PENDING, null, null);

        return createdOrder;
    }

    @Override
    public Optional<OrderModel> getOrderById(Long idOrder) {
        return orderPersistencePort.getOrderById(idOrder);
    }

    @Override
    public List<OrderModel> getOrdersByChefId(Long chefId) {
        return orderPersistencePort.getOrdersByChefId(chefId);
    }

    @Override
    public Page<OrderModel> getOrdersByStateAndRestaurant(String stateOrder, Long idRestaurant, Pageable pageable) {
        validateRestaurantAndState(stateOrder, idRestaurant);
        return orderPersistencePort.getOrdersByStateAndRestaurant(stateOrder, idRestaurant, pageable);
    }

    @Override
    public OrderModel assignEmployeeToOrder(Long orderId, Long employeeId) {
        OrderModel order = getOrderByIdOrThrow(orderId);
        validatorServicePort.validatePendingOrder(order);

        UserDto employee = getUserById(employeeId);

        updateOrderState(order, Constants.IN_PREPARATION);
        order.setChefId(employeeId);

        logTraceability(order, Constants.PENDING, Constants.IN_PREPARATION, employeeId, employee.getEmail());

        return orderPersistencePort.saveOrder(order);
    }

    @Override
    public OrderModel markOrderAsReadyAndNotify(Long orderId) {
        OrderModel order = getOrderByIdOrThrow(orderId);
        validateState(order, Constants.IN_PREPARATION);
        String pin = generatePin();
        updateOrderState(order, Constants.READY);
        order.setSecurityPin(pin);

        notifyCustomer(order.getCustomerId(), pin);
        // Obtener el correo del empleado
        String emailEmployee = null;
        if (order.getChefId() != null) {
            UserDto employee = getUserById(order.getChefId());
            emailEmployee = employee.getEmail();
        }
        // Registrar trazabilidad
        logTraceability(order, Constants.IN_PREPARATION, Constants.READY, order.getChefId(), emailEmployee);

        return orderPersistencePort.saveOrder(order);
    }


    @Override
    public OrderModel markOrderAsDelivered(Long orderId, String providedPin) {
        OrderModel order = getOrderByIdOrThrow(orderId);
        validateState(order, Constants.READY);
        validatePin(order.getSecurityPin(), providedPin);

        updateOrderState(order, Constants.DELIVERED);

        UserDto employee = getUserById(order.getChefId());
        logTraceability(order, Constants.READY, Constants.DELIVERED, order.getChefId(), employee.getEmail());

        return orderPersistencePort.saveOrder(order);
    }

    @Override
    public OrderModel cancelOrder(Long orderId, Long customerId) {
        OrderModel order = getOrderByIdOrThrow(orderId);

        if (!order.getCustomerId().equals(customerId)) {
            throw new IllegalStateException("You can only cancel your own orders.");
        }

        validatorServicePort.validatePendingOrder(order);

        updateOrderState(order, Constants.CANCELED);

        logTraceability(order, Constants.PENDING, Constants.CANCELED, null, null);

        return orderPersistencePort.saveOrder(order);
    }

    // Métodos Auxiliares

    private void validateCreateOrder(OrderModel orderModel) {
        if (orderModel.getRestaurant() == null || orderModel.getRestaurant().getIdRestaurant() == null) {
            throw new IllegalArgumentException("Restaurant ID is required.");
        }
        validatorServicePort.validateOrder(orderModel);
    }

    private void validateRestaurantAndState(String stateOrder, Long idRestaurant) {
        if (stateOrder == null || stateOrder.isBlank()) {
            throw new IllegalArgumentException("State order is required.");
        }
        if (idRestaurant == null) {
            throw new IllegalArgumentException("Restaurant ID is required.");
        }
    }

    private void validateState(OrderModel order, String expectedState) {
        if (!expectedState.equals(order.getStateOrder())) {
            throw new IllegalStateException("Order must be in state " + expectedState + ".");
        }
    }

    private void validatePin(String actualPin, String providedPin) {
        if (!actualPin.equals(providedPin)) {
            throw new IllegalStateException("Invalid security PIN.");
        }
    }

    private OrderModel getOrderByIdOrThrow(Long orderId) {
        return orderPersistencePort.getOrderById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    private void updateOrderState(OrderModel order, String newState) {
        order.setStateOrder(newState);
    }

    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (Long) authentication.getDetails();
    }

    private UserDto getUserById(Long userId) {
        return userClientPort.getUserById(userId);
    }

    private void notifyCustomer(Long customerId, String pin) {
        UserDto customer = getUserById(customerId);
        if (customer.getPhoneNumber() == null) {
            throw new IllegalStateException("Customer information is missing.");
        }
        String message = String.format("Your order is ready! Please use PIN: %s to collect it.", pin);
        smsClientPort.sendSms(customer.getPhoneNumber(), message);
    }

    private void logTraceability(OrderModel order, String previousState, String newState, Long employeeId, String emailEmployee) {
        TraceabilityRequestDto traceabilityRequest = TraceabilityRequestDto.builder()
                .idOrder(order.getIdOrder())
                .customerId(order.getCustomerId())
                .emailCustomer(getUserById(order.getCustomerId()).getEmail())
                .previousState(previousState)
                .newState(newState)
                .employeeId(employeeId)
                .emailEmployee(emailEmployee)
                .build();
        traceabilityFeignClient.logTraceability(traceabilityRequest);
    }

    private String generatePin() {
        return RANDOM.nextInt(9000) + 1000 + ""; // Genera un PIN de 4 dígitos
    }

}


