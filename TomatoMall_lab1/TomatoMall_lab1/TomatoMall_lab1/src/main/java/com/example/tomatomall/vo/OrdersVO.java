package com.example.tomatomall.vo;
import com.example.tomatomall.po.Orders;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class OrdersVO {
    private Integer orderId;

    private String userName;

    private BigDecimal totalAmount;

    private String paymentMethod;

    private LocalDateTime createTime;

    private String status;

    public Orders toPO(){
        Orders orders=new Orders();
        orders.setOrderId(this.orderId);
        orders.setTotalAmount(this.totalAmount);
        orders.setPaymentMethod(this.paymentMethod);
        orders.setStatus(this.status);
        orders.setCreateTime(this.createTime);
        return orders;
    }
}