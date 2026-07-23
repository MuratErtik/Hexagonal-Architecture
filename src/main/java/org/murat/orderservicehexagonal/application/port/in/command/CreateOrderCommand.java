package org.murat.orderservicehexagonal.application.port.in.command;

// it is request class using in controller
public record CreateOrderCommand(
        String shippingAddress,
        String cardHolderName,
        String cardNumber,
        String expireMonth,
        String expireYear,
        String cvc,
        String buyerEmail
) {}

