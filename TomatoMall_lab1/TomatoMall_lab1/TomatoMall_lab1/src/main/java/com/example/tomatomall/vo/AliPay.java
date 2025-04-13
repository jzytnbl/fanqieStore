package com.example.tomatomall.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AliPay {
    private String paymentForm;
    private String orderId;
    private BigDecimal totalAmount;
    private String paymentMethod = "Alipay";
}