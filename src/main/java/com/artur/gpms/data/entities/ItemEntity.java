package com.artur.gpms.data.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;

@Document(collection = "tb_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemEntity {

    private String product;

    private String quantity;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal price;

    private String description;
}
