package com.example.tomatomall.vo;

import java.math.BigDecimal;
import java.util.List;

import com.example.tomatomall.po.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductVO {
    private Integer id;

    private String title;

    private BigDecimal price;

    private Double rate;

    private String description;

    private String cover;

    private String detail;

    private List<SpecificationVO> specifications; // 新增

    private StockpileVO stockpile;

    public Product toPO(){
        Product product=new Product();
        product.setId(this.id);
        product.setTitle(this.title);
        product.setPrice(this.price);
        product.setRate(this.rate);
        product.setDescription(this.description);
        product.setCover(this.cover);
        product.setDetail(this.detail);
        return product;
    }
}