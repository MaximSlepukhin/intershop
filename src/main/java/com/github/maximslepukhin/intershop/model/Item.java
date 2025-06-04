package com.github.maximslepukhin.intershop.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private Long id;
    private String title;
    private String description;
    private double price;
    private String imgPath;
    private int count;
}